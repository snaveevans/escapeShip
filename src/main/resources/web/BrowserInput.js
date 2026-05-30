const GAMEPLAY_KEYS = new Set(['a', 'd', 'arrowleft', 'arrowright', 'p', 'r']);

export class BrowserInput {
  constructor(world, target = window, root = globalThis.document, canvas = null) {
    this.world = world;
    this.canvasPointers = new Map();
    this.fireChordActive = false;
    this.canvas = canvas || root?.querySelector?.('#game') || null;

    target.addEventListener('keydown', event => this.keyDown(event));
    target.addEventListener('keyup', event => this.keyUp(event));
    this.bindTouchControls(root);
    this.bindCanvasControls(this.canvas);
    this.syncControlLabels();
  }

  keyDown(event) {
    if (this.isGameplayKey(event)) {
      event.preventDefault();
    }

    const key = event.key.toLowerCase();

    if (key === 'a' || event.key === 'ArrowLeft') {
      this.world.input.left = true;
    }
    if (key === 'd' || event.key === 'ArrowRight') {
      this.world.input.right = true;
    }
  }

  keyUp(event) {
    if (this.isGameplayKey(event)) {
      event.preventDefault();
    }

    const key = event.key.toLowerCase();

    if (key === 'a' || event.key === 'ArrowLeft') {
      this.world.input.left = false;
    }
    if (key === 'd' || event.key === 'ArrowRight') {
      this.world.input.right = false;
    }
    if (event.code === 'Space') {
      this.world.fire();
    }
    if (key === 'p') {
      this.world.pause();
      this.syncControlLabels();
    }
    if (key === 'r') {
      this.restartGame(this.world.gameOver);
      this.syncControlLabels();
    }
  }

  isGameplayKey(event) {
    return event.code === 'Space' || GAMEPLAY_KEYS.has(event.key.toLowerCase());
  }

  bindTouchControls(root) {
    if (!root) {
      return;
    }

    this.bindHoldControl(root.querySelector('[data-control="left"]'), 'left');
    this.bindHoldControl(root.querySelector('[data-control="right"]'), 'right');
    this.bindActionControls(root.querySelectorAll('[data-control="fire"]'), () => this.world.fire());
    this.bindActionControls(root.querySelectorAll('[data-control="pause"]'), () => this.activatePrimaryControl());
    this.bindActionControls(root.querySelectorAll('[data-control="restart"]'), () => {
      this.restartGame(false);
      this.syncControlLabels();
    });
    this.configureDebugControls(root);
  }

  configureDebugControls(root) {
    const controls = root.querySelector?.('[data-debug-controls]');
    if (!controls) {
      return;
    }

    const params = new URLSearchParams(globalThis.location?.search || '');
    controls.hidden = !params.has('debugControls');
  }

  bindCanvasControls(canvas) {
    if (!canvas) {
      return;
    }

    canvas.addEventListener('pointerdown', event => this.canvasPointerDown(event));
    canvas.addEventListener('pointermove', event => this.canvasPointerMove(event));
    canvas.addEventListener('pointerup', event => this.canvasPointerEnd(event));
    canvas.addEventListener('pointercancel', event => this.canvasPointerEnd(event));
    canvas.addEventListener('pointerleave', event => this.canvasPointerLeave(event));
  }

  canvasPointerDown(event) {
    if (!this.canvas) {
      return;
    }

    event.preventDefault();
    this.canvasPointers.set(event.pointerId, this.sideForPoint(this.canvasPoint(event)));
    this.syncCanvasInput();
    this.capturePointer(event);
  }

  canvasPointerMove(event) {
    if (!this.canvasPointers.has(event.pointerId)) {
      return;
    }

    event.preventDefault();
    this.canvasPointers.set(event.pointerId, this.sideForPoint(this.canvasPoint(event)));
    this.syncCanvasInput();
  }

  canvasPointerEnd(event) {
    if (this.canvasPointers.has(event.pointerId)) {
      event.preventDefault();
      this.canvasPointers.delete(event.pointerId);
      this.syncCanvasInput();
    }
  }

  canvasPointerLeave(event) {
    if (!this.canvasPointers.has(event.pointerId)) {
      return;
    }

    event.preventDefault();
    this.canvasPointers.delete(event.pointerId);
    this.syncCanvasInput();
  }

  canvasPoint(event) {
    const bounds = this.canvas.getBoundingClientRect();
    return {
      x: event.clientX - bounds.left,
      y: event.clientY - bounds.top,
      width: bounds.width,
      height: bounds.height
    };
  }

  sideForPoint(point) {
    return point.x < point.width / 2 ? 'left' : 'right';
  }

  syncCanvasInput() {
    let hasLeft = false;
    let hasRight = false;

    for (const side of this.canvasPointers.values()) {
      hasLeft ||= side === 'left';
      hasRight ||= side === 'right';
    }

    this.world.input.left = hasLeft;
    this.world.input.right = hasRight;

    const fireChordPressed = hasLeft && hasRight;
    if (fireChordPressed && !this.fireChordActive) {
      this.world.fire();
    }
    this.fireChordActive = fireChordPressed;
  }

  capturePointer(event) {
    this.canvas?.setPointerCapture?.(event.pointerId);
  }

  bindHoldControl(element, inputName) {
    if (!element) {
      return;
    }

    const start = event => {
      event.preventDefault();
      this.world.input[inputName] = true;
    };
    const stop = event => {
      event.preventDefault();
      this.world.input[inputName] = false;
    };

    element.addEventListener('pointerdown', start);
    element.addEventListener('pointerup', stop);
    element.addEventListener('pointercancel', stop);
    element.addEventListener('pointerleave', stop);
  }

  bindActionControls(elements, action) {
    elements.forEach(element => this.bindActionControl(element, action));
  }

  bindActionControl(element, action) {
    if (!element) {
      return;
    }

    let pointerActivated = false;
    element.addEventListener('pointerdown', event => {
      event.preventDefault();
      pointerActivated = true;
      action();
    });
    element.addEventListener('click', event => {
      event.preventDefault();
      if (pointerActivated) {
        pointerActivated = false;
        return;
      }
      action();
    });
  }

  activatePrimaryControl() {
    if (this.world.gameOver) {
      this.restartGame(true);
    } else {
      this.world.pause();
    }
    this.syncControlLabels();
  }

  restartGame(startImmediately) {
    this.world.restart(startImmediately);
  }

  syncControlLabels() {
    if (!this.canvas?.ownerDocument) {
      return;
    }

    this.canvas.ownerDocument.querySelectorAll('[data-control="pause"]').forEach(button => {
      const label = this.world.gameOver ? 'Restart' : (this.world.paused ? 'Resume' : 'Pause');
      button.textContent = label;
      button.setAttribute('aria-label', `${label} game`);
    });
  }
}
