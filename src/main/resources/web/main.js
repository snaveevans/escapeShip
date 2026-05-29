import { DEFAULT_UPDATES_PER_SECOND, GameWorld } from './escape-ship-core.js';
import { BrowserInput } from './BrowserInput.js';
import { CanvasRenderer } from './CanvasRenderer.js';

const canvas = document.getElementById('game');
// Gameplay remains in the canvas element's authored coordinate system;
// CanvasRenderer maps those virtual units onto the displayed DPR-scaled pixels.
const virtualSize = {
  width: Number(canvas.getAttribute('width')),
  height: Number(canvas.getAttribute('height'))
};
const world = new GameWorld(virtualSize);
const renderer = new CanvasRenderer(canvas, world, {
  virtualWidth: virtualSize.width,
  virtualHeight: virtualSize.height
});
new BrowserInput(world);

const millisecondsPerUpdate = 1000 / DEFAULT_UPDATES_PER_SECOND;
const maximumElapsedMilliseconds = 250;

let accumulatedElapsedMilliseconds = 0;
let previousTimestamp = null;

function animate(timestamp) {
  if (previousTimestamp === null) {
    previousTimestamp = timestamp;
  }

  const elapsedMilliseconds = Math.min(
    timestamp - previousTimestamp,
    maximumElapsedMilliseconds,
  );
  previousTimestamp = timestamp;

  accumulatedElapsedMilliseconds += elapsedMilliseconds;
  while (accumulatedElapsedMilliseconds >= millisecondsPerUpdate) {
    world.update();
    accumulatedElapsedMilliseconds -= millisecondsPerUpdate;
  }

  renderer.render();
  requestAnimationFrame(animate);
}

requestAnimationFrame(animate);
