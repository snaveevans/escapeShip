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

setInterval(() => {
  world.update();
  renderer.render();
}, 1000 / DEFAULT_UPDATES_PER_SECOND);

window.addEventListener('resize', () => renderer.renderIfBackingSizeChanged());
renderer.render();
