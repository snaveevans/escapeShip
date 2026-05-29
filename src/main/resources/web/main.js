import { DEFAULT_UPDATES_PER_SECOND, GameWorld } from './escape-ship-core.js';
import { BrowserInput } from './BrowserInput.js';
import { CanvasRenderer } from './CanvasRenderer.js';

const canvas = document.getElementById('game');
const virtualSize = {
  width: canvas.width,
  height: canvas.height
};
const world = new GameWorld(virtualSize);
const renderer = new CanvasRenderer(canvas, world, {
  virtualWidth: virtualSize.width,
  virtualHeight: virtualSize.height
});
new BrowserInput(world);

setInterval(() => {
  world.update();
  renderer.render();
}, 1000 / DEFAULT_UPDATES_PER_SECOND);

window.addEventListener('resize', () => renderer.renderIfBackingSizeChanged());
renderer.render();
