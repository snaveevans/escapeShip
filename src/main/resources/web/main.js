import { DEFAULT_UPDATES_PER_SECOND, GameWorld } from './escape-ship-core.js';
import { BrowserInput } from './BrowserInput.js';
import { CanvasRenderer } from './CanvasRenderer.js';

const canvas = document.getElementById('game');
const world = new GameWorld({ width: canvas.width, height: canvas.height });
const renderer = new CanvasRenderer(canvas, world);
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
