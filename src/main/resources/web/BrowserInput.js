export class BrowserInput {
  constructor(world, target = window) {
    this.world = world;
    target.addEventListener('keydown', event => this.keyDown(event));
    target.addEventListener('keyup', event => this.keyUp(event));
  }

  keyDown(event) {
    if (event.key === 'a' || event.key === 'ArrowLeft') {
      this.world.input.left = true;
    }
    if (event.key === 'd' || event.key === 'ArrowRight') {
      this.world.input.right = true;
    }
  }

  keyUp(event) {
    if (event.key === 'a' || event.key === 'ArrowLeft') {
      this.world.input.left = false;
    }
    if (event.key === 'd' || event.key === 'ArrowRight') {
      this.world.input.right = false;
    }
    if (event.code === 'Space') {
      this.world.fire();
    }
    if (event.key === 'p') {
      this.world.pause();
    }
    if (event.key === 'r') {
      this.world.restart();
    }
  }
}
