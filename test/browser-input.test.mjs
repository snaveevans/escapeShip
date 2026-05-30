import assert from 'node:assert/strict';
import test from 'node:test';
import { BrowserInput } from '../src/main/resources/web/BrowserInput.js';

class EventTargetStub {
  constructor() {
    this.listeners = new Map();
  }

  addEventListener(type, listener) {
    const listeners = this.listeners.get(type) || [];
    listeners.push(listener);
    this.listeners.set(type, listeners);
  }

  dispatch(type, event = {}) {
    const fullEvent = {
      key: '',
      code: '',
      pointerId: 1,
      clientX: 0,
      clientY: 0,
      defaultPrevented: false,
      preventDefault() {
        this.defaultPrevented = true;
      },
      ...event
    };

    for (const listener of this.listeners.get(type) || []) {
      listener(fullEvent);
    }
    return fullEvent;
  }
}

class ElementStub extends EventTargetStub {
  constructor() {
    super();
    this.hidden = false;
    this.textContent = '';
    this.attributes = new Map();
  }

  setAttribute(name, value) {
    this.attributes.set(name, value);
  }
}

class CanvasStub extends ElementStub {
  constructor(document) {
    super();
    this.ownerDocument = document;
    this.capturedPointers = [];
  }

  getBoundingClientRect() {
    return { left: 10, top: 20, width: 200, height: 400 };
  }

  setPointerCapture(pointerId) {
    this.capturedPointers.push(pointerId);
  }
}

function createWorld() {
  return {
    input: { left: false, right: false },
    paused: true,
    fireCount: 0,
    pauseCount: 0,
    restartCount: 0,
    fire() {
      this.fireCount++;
    },
    pause() {
      this.pauseCount++;
      this.paused = !this.paused;
    },
    restart() {
      this.restartCount++;
    }
  };
}

function createHarness(search = '') {
  const previousLocation = globalThis.location;
  Object.defineProperty(globalThis, 'location', {
    configurable: true,
    value: { search }
  });

  const debugControls = new ElementStub();
  const pauseButtons = [new ElementStub(), new ElementStub()];
  const root = {
    querySelector(selector) {
      if (selector === '[data-debug-controls]') {
        return debugControls;
      }
      return null;
    },
    querySelectorAll(selector) {
      return selector === '[data-control="pause"]' ? pauseButtons : [];
    }
  };
  const canvas = new CanvasStub(root);
  const target = new EventTargetStub();
  const world = createWorld();
  const input = new BrowserInput(world, target, root, canvas);

  return {
    canvas,
    debugControls,
    input,
    pauseButtons,
    restoreLocation() {
      Object.defineProperty(globalThis, 'location', {
        configurable: true,
        value: previousLocation
      });
    },
    target,
    world
  };
}

test('left-half drag right sets right input', () => {
  const harness = createHarness();
  try {
    harness.canvas.dispatch('pointerdown', { pointerId: 11, clientX: 50, clientY: 120 });
    harness.canvas.dispatch('pointermove', { pointerId: 11, clientX: 90, clientY: 120 });

    assert.equal(harness.world.input.left, false);
    assert.equal(harness.world.input.right, true);
  } finally {
    harness.restoreLocation();
  }
});

test('left-half drag left sets left input', () => {
  const harness = createHarness();
  try {
    harness.canvas.dispatch('pointerdown', { pointerId: 12, clientX: 90, clientY: 120 });
    harness.canvas.dispatch('pointermove', { pointerId: 12, clientX: 50, clientY: 120 });

    assert.equal(harness.world.input.left, true);
    assert.equal(harness.world.input.right, false);
  } finally {
    harness.restoreLocation();
  }
});

test('movement deadzone clears both directions', () => {
  const harness = createHarness();
  try {
    harness.canvas.dispatch('pointerdown', { pointerId: 13, clientX: 50, clientY: 120 });
    harness.canvas.dispatch('pointermove', { pointerId: 13, clientX: 90, clientY: 120 });
    harness.canvas.dispatch('pointermove', { pointerId: 13, clientX: 60, clientY: 120 });

    assert.equal(harness.world.input.left, false);
    assert.equal(harness.world.input.right, false);
  } finally {
    harness.restoreLocation();
  }
});

test('movement crossing into right half neutralizes steering without firing', () => {
  const harness = createHarness();
  try {
    harness.canvas.dispatch('pointerdown', { pointerId: 18, clientX: 50, clientY: 120 });
    harness.canvas.dispatch('pointermove', { pointerId: 18, clientX: 90, clientY: 120 });
    harness.canvas.dispatch('pointermove', { pointerId: 18, clientX: 130, clientY: 120 });

    assert.equal(harness.world.input.left, false);
    assert.equal(harness.world.input.right, false);
    assert.equal(harness.world.fireCount, 0);
  } finally {
    harness.restoreLocation();
  }
});

test('movement release and leave clear input', () => {
  const releaseHarness = createHarness();
  try {
    releaseHarness.canvas.dispatch('pointerdown', { pointerId: 14, clientX: 50, clientY: 120 });
    releaseHarness.canvas.dispatch('pointermove', { pointerId: 14, clientX: 90, clientY: 120 });
    releaseHarness.canvas.dispatch('pointerup', { pointerId: 14, clientX: 90, clientY: 120 });

    assert.equal(releaseHarness.world.input.left, false);
    assert.equal(releaseHarness.world.input.right, false);
  } finally {
    releaseHarness.restoreLocation();
  }

  const leaveHarness = createHarness();
  try {
    leaveHarness.canvas.dispatch('pointerdown', { pointerId: 15, clientX: 90, clientY: 120 });
    leaveHarness.canvas.dispatch('pointermove', { pointerId: 15, clientX: 50, clientY: 120 });
    leaveHarness.canvas.dispatch('pointerleave', { pointerId: 15, clientX: 50, clientY: 120 });

    assert.equal(leaveHarness.world.input.left, false);
    assert.equal(leaveHarness.world.input.right, false);
  } finally {
    leaveHarness.restoreLocation();
  }
});

test('right-half press fires exactly once', () => {
  const harness = createHarness();
  try {
    harness.canvas.dispatch('pointerdown', { pointerId: 16, clientX: 170, clientY: 120 });
    harness.canvas.dispatch('pointerup', { pointerId: 16, clientX: 170, clientY: 120 });

    assert.equal(harness.world.fireCount, 1);
    assert.equal(harness.world.input.left, false);
    assert.equal(harness.world.input.right, false);
  } finally {
    harness.restoreLocation();
  }
});

test('movement pointer and fire pointer do not interfere', () => {
  const harness = createHarness();
  try {
    harness.canvas.dispatch('pointerdown', { pointerId: 21, clientX: 50, clientY: 120 });
    harness.canvas.dispatch('pointermove', { pointerId: 21, clientX: 90, clientY: 120 });
    harness.canvas.dispatch('pointerdown', { pointerId: 22, clientX: 170, clientY: 120 });
    harness.canvas.dispatch('pointerup', { pointerId: 22, clientX: 170, clientY: 120 });

    assert.equal(harness.world.fireCount, 1);
    assert.equal(harness.world.input.right, true);

    harness.canvas.dispatch('pointerup', { pointerId: 21, clientX: 90, clientY: 120 });
    assert.equal(harness.world.input.right, false);
  } finally {
    harness.restoreLocation();
  }
});

test('debug controls show only with debugControls query parameter', () => {
  const hiddenHarness = createHarness('');
  try {
    assert.equal(hiddenHarness.debugControls.hidden, true);
  } finally {
    hiddenHarness.restoreLocation();
  }

  const visibleHarness = createHarness('?debugControls=1');
  try {
    assert.equal(visibleHarness.debugControls.hidden, false);
  } finally {
    visibleHarness.restoreLocation();
  }
});

test('all pause controls stay labeled and bound', () => {
  const harness = createHarness('?debugControls=1');
  try {
    assert.deepEqual(harness.pauseButtons.map(button => button.textContent), ['Resume', 'Resume']);

    harness.pauseButtons[1].dispatch('pointerdown', { pointerId: 41 });

    assert.equal(harness.world.pauseCount, 1);
    assert.deepEqual(harness.pauseButtons.map(button => button.textContent), ['Pause', 'Pause']);
  } finally {
    harness.restoreLocation();
  }
});
