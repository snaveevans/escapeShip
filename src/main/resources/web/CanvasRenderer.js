export class CanvasRenderer {
  constructor(canvas, world, options = {}) {
    this.canvas = canvas;
    this.world = world;
    this.context = canvas.getContext('2d');
    this.virtualWidth = options.virtualWidth ?? world.size.width;
    this.virtualHeight = options.virtualHeight ?? world.size.height;

    this.syncDisplaySize();
  }

  render() {
    this.resizeBackingStoreToDisplaySize();

    const context = this.context;
    context.save();
    this.applyVirtualScale();

    context.fillStyle = 'lightgray';
    context.fillRect(0, 0, this.virtualWidth, this.virtualHeight);

    this.world.asteroids.forEach(asteroid => this.drawPolygon(asteroid.shape(), 'black'));
    context.fillStyle = 'red';
    this.world.lasers.forEach(laser => context.fillRect(laser.x, laser.y, laser.width, laser.height));
    this.drawPolygon(this.world.ship.shape(), 'blue');
    this.drawStats();
    if (this.world.paused) {
      this.drawPause();
    }

    context.restore();
  }

  syncDisplaySize() {
    const viewport = globalThis.window || {};
    const maxWidth = Math.min(this.virtualWidth, (viewport.innerWidth || this.virtualWidth) * 0.95);
    const maxHeight = Math.min(this.virtualHeight, (viewport.innerHeight || this.virtualHeight) * 0.95);
    const scale = Math.min(maxWidth / this.virtualWidth, maxHeight / this.virtualHeight);

    this.canvas.style.width = `${Math.round(this.virtualWidth * scale)}px`;
    this.canvas.style.height = `${Math.round(this.virtualHeight * scale)}px`;
  }

  resizeBackingStoreToDisplaySize() {
    this.syncDisplaySize();

    const { width, height } = this.canvas.getBoundingClientRect();
    const pixelRatio = (globalThis.window && globalThis.window.devicePixelRatio) || 1;
    const displayWidth = Math.max(1, Math.round(width * pixelRatio));
    const displayHeight = Math.max(1, Math.round(height * pixelRatio));

    if (this.canvas.width !== displayWidth || this.canvas.height !== displayHeight) {
      this.canvas.width = displayWidth;
      this.canvas.height = displayHeight;
    }
  }

  applyVirtualScale() {
    this.context.setTransform(
      this.canvas.width / this.virtualWidth,
      0,
      0,
      this.canvas.height / this.virtualHeight,
      0,
      0
    );
  }

  renderIfBackingSizeChanged() {
    const previousWidth = this.canvas.width;
    const previousHeight = this.canvas.height;
    this.resizeBackingStoreToDisplaySize();
    if (this.canvas.width !== previousWidth || this.canvas.height !== previousHeight) {
      this.render();
    }
  }

  drawPolygon(shape, color) {
    const context = this.context;
    context.strokeStyle = color;
    context.beginPath();
    shape.points.forEach((point, index) => {
      if (index === 0) {
        context.moveTo(point.x, point.y);
      } else {
        context.lineTo(point.x, point.y);
      }
    });
    context.closePath();
    context.stroke();
  }

  drawStats() {
    const context = this.context;
    const ship = this.world.ship;
    context.fillStyle = 'black';
    context.fillText(`Distance: ${Math.floor(ship.distance)} Level: ${this.world.level} Asteroids: ${this.world.asteroidsHit}`, 3, 13);

    context.strokeStyle = 'red';
    let x = 5;
    for (let i = 0; i < ship.ammo; i++) {
      x += 5;
      context.strokeRect(this.virtualWidth - x, 5, 1, 7);
    }
    context.strokeRect(this.virtualWidth - 9 - ship.rechargeMax + ship.rechargeRate, 1, ship.rechargeMax - ship.rechargeRate, 1);
  }

  drawPause() {
    const context = this.context;
    context.fillStyle = 'black';
    context.textAlign = 'center';
    const message = this.world.firstTime
      ? 'Welcome! Use A/D or arrows, Space to fire, P to pause'
      : (this.world.gameOver ? 'Game Over: press R' : 'Paused: press P or R');
    context.fillText(message, this.virtualWidth / 2, this.virtualHeight / 2);
    context.textAlign = 'left';
  }
}
