const GAMEPLAY_KEYS = new Set(['a', 'd', 'arrowleft', 'arrowright', 'p', 'r']);

export class BrowserInput {
  constructor(world, target = window, root = globalThis.document) {
    this.world = world;
    target.addEventListener('keydown', event => this.keyDown(event));
    target.addEventListener('keyup', event => this.keyUp(event));
    this.bindTouchControls(root);
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
    }
    if (key === 'r') {
      this.world.restart();
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
    this.bindActionControl(root.querySelector('[data-control="fire"]'), () => this.world.fire());
    this.bindActionControl(root.querySelector('[data-control="pause"]'), () => this.world.pause());
    this.bindActionControl(root.querySelector('[data-control="restart"]'), () => this.world.restart());
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
}
