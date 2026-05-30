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
    gameOver: false,
    fireCount: 0,
    pauseCount: 0,
    restartCount: 0,
    restartImmediately: [],
    fire() {
      this.fireCount++;
    },
    pause() {
      this.pauseCount++;
      this.paused = !this.paused;
    },
    restart(startImmediately = false) {
      this.restartCount++;
      this.restartImmediately.push(startImmediately);
      this.gameOver = false;
      this.paused = !startImmediately;
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

test('left-half press sets left input until release', () => {
  const harness = createHarness();
  try {
    harness.canvas.dispatch('pointerdown', { pointerId: 11, clientX: 50, clientY: 120 });

    assert.equal(harness.world.input.left, true);
    assert.equal(harness.world.input.right, false);
    assert.equal(harness.world.fireCount, 0);

    harness.canvas.dispatch('pointerup', { pointerId: 11, clientX: 50, clientY: 120 });
    assert.equal(harness.world.input.left, false);
    assert.equal(harness.world.input.right, false);
  } finally {
    harness.restoreLocation();
  }
});

test('right-half press sets right input without firing', () => {
  const harness = createHarness();
  try {
    harness.canvas.dispatch('pointerdown', { pointerId: 12, clientX: 170, clientY: 120 });

    assert.equal(harness.world.input.left, false);
    assert.equal(harness.world.input.right, true);
    assert.equal(harness.world.fireCount, 0);

    harness.canvas.dispatch('pointerup', { pointerId: 12, clientX: 170, clientY: 120 });
    assert.equal(harness.world.input.right, false);
  } finally {
    harness.restoreLocation();
  }
});

test('pressing both halves fires once and holds both directions', () => {
  const harness = createHarness();
  try {
    harness.canvas.dispatch('pointerdown', { pointerId: 13, clientX: 50, clientY: 120 });
    harness.canvas.dispatch('pointerdown', { pointerId: 14, clientX: 170, clientY: 120 });

    assert.equal(harness.world.input.left, true);
    assert.equal(harness.world.input.right, true);
    assert.equal(harness.world.fireCount, 1);

    harness.canvas.dispatch('pointermove', { pointerId: 14, clientX: 175, clientY: 120 });
    assert.equal(harness.world.fireCount, 1);
  } finally {
    harness.restoreLocation();
  }
});

test('both-half fire chord can repeat after release', () => {
  const harness = createHarness();
  try {
    harness.canvas.dispatch('pointerdown', { pointerId: 15, clientX: 50, clientY: 120 });
    harness.canvas.dispatch('pointerdown', { pointerId: 16, clientX: 170, clientY: 120 });
    harness.canvas.dispatch('pointerup', { pointerId: 16, clientX: 170, clientY: 120 });
    harness.canvas.dispatch('pointerdown', { pointerId: 17, clientX: 170, clientY: 120 });

    assert.equal(harness.world.fireCount, 2);
  } finally {
    harness.restoreLocation();
  }
});

test('single pointer moving between halves changes direction without firing', () => {
  const harness = createHarness();
  try {
    harness.canvas.dispatch('pointerdown', { pointerId: 18, clientX: 50, clientY: 120 });
    harness.canvas.dispatch('pointermove', { pointerId: 18, clientX: 170, clientY: 120 });

    assert.equal(harness.world.input.left, false);
    assert.equal(harness.world.input.right, true);
    assert.equal(harness.world.fireCount, 0);
  } finally {
    harness.restoreLocation();
  }
});

test('canvas pointer release and leave clear input', () => {
  const releaseHarness = createHarness();
  try {
    releaseHarness.canvas.dispatch('pointerdown', { pointerId: 19, clientX: 50, clientY: 120 });
    releaseHarness.canvas.dispatch('pointerup', { pointerId: 19, clientX: 50, clientY: 120 });

    assert.equal(releaseHarness.world.input.left, false);
    assert.equal(releaseHarness.world.input.right, false);
  } finally {
    releaseHarness.restoreLocation();
  }

  const leaveHarness = createHarness();
  try {
    leaveHarness.canvas.dispatch('pointerdown', { pointerId: 20, clientX: 170, clientY: 120 });
    leaveHarness.canvas.dispatch('pointerleave', { pointerId: 20, clientX: 170, clientY: 120 });

    assert.equal(leaveHarness.world.input.left, false);
    assert.equal(leaveHarness.world.input.right, false);
  } finally {
    leaveHarness.restoreLocation();
  }
});

test('releasing one pointer during a fire chord keeps the other side active', () => {
  const harness = createHarness();
  try {
    harness.canvas.dispatch('pointerdown', { pointerId: 21, clientX: 50, clientY: 120 });
    harness.canvas.dispatch('pointerdown', { pointerId: 22, clientX: 170, clientY: 120 });
    harness.canvas.dispatch('pointerup', { pointerId: 22, clientX: 170, clientY: 120 });

    assert.equal(harness.world.fireCount, 1);
    assert.equal(harness.world.input.left, true);
    assert.equal(harness.world.input.right, false);

    harness.canvas.dispatch('pointerup', { pointerId: 21, clientX: 50, clientY: 120 });
    assert.equal(harness.world.input.left, false);
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

test('game-over primary controls relabel to restart and immediately start a new game', () => {
  const harness = createHarness('?debugControls=1');
  try {
    harness.world.gameOver = true;
    harness.world.paused = true;
    harness.input.syncControlLabels();

    assert.deepEqual(harness.pauseButtons.map(button => button.textContent), ['Restart', 'Restart']);

    harness.pauseButtons[0].dispatch('pointerdown', { pointerId: 42 });

    assert.equal(harness.world.restartCount, 1);
    assert.deepEqual(harness.world.restartImmediately, [true]);
    assert.equal(harness.world.gameOver, false);
    assert.equal(harness.world.paused, false);
    assert.deepEqual(harness.pauseButtons.map(button => button.textContent), ['Pause', 'Pause']);
  } finally {
    harness.restoreLocation();
  }
});

test('keyboard restart immediately starts after game over', () => {
  const harness = createHarness();
  try {
    harness.world.gameOver = true;
    harness.world.paused = true;

    harness.target.dispatch('keyup', { key: 'r' });

    assert.equal(harness.world.restartCount, 1);
    assert.deepEqual(harness.world.restartImmediately, [true]);
    assert.equal(harness.world.paused, false);
  } finally {
    harness.restoreLocation();
  }
});
