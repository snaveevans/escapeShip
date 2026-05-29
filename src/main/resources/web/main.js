import { DEFAULT_UPDATES_PER_SECOND, GameWorld } from './escape-ship-core.js';
import { BrowserInput } from './BrowserInput.js';
import { CanvasRenderer } from './CanvasRenderer.js';

const canvas = document.getElementById('game');
const world = new GameWorld({ width: canvas.width, height: canvas.height });
const renderer = new CanvasRenderer(canvas, world);
new BrowserInput(world);

setInterval(() => {
  world.update();
  renderer.render();
}, 1000 / DEFAULT_UPDATES_PER_SECOND);

renderer.render();
