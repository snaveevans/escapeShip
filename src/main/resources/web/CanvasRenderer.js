export class CanvasRenderer {
  constructor(canvas, world) {
    this.canvas = canvas;
    this.world = world;
    this.context = canvas.getContext('2d');
  }

  render() {
    const context = this.context;
    const width = this.canvas.width;
    const height = this.canvas.height;
    context.fillStyle = 'lightgray';
    context.fillRect(0, 0, width, height);

    this.world.asteroids.forEach(asteroid => this.drawPolygon(asteroid.shape(), 'black'));
    context.fillStyle = 'red';
    this.world.lasers.forEach(laser => context.fillRect(laser.x, laser.y, laser.width, laser.height));
    this.drawPolygon(this.world.ship.shape(), 'blue');
    this.drawStats();
    if (this.world.paused) {
      this.drawPause();
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
      context.strokeRect(this.canvas.width - x, 5, 1, 7);
    }
    context.strokeRect(this.canvas.width - 9 - ship.rechargeMax + ship.rechargeRate, 1, ship.rechargeMax - ship.rechargeRate, 1);
  }

  drawPause() {
    const context = this.context;
    context.fillStyle = 'black';
    context.textAlign = 'center';
    const message = this.world.firstTime
      ? 'Welcome! Use A/D or arrows, Space to fire, P to pause'
      : (this.world.gameOver ? 'Game Over: press R' : 'Paused: press P or R');
    context.fillText(message, this.canvas.width / 2, this.canvas.height / 2);
    context.textAlign = 'left';
  }
}
