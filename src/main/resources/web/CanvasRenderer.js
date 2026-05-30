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
    context.restore();

    context.save();
    this.applyHudScale();
    this.drawStats();
    if (this.world.paused) {
      this.drawPause();
    }
    context.restore();
  }

  syncDisplaySize() {
    const displayBounds = this.displayBounds();
    const scale = Math.min(
      1,
      displayBounds.width / this.virtualWidth,
      displayBounds.height / this.virtualHeight
    );

    // Pin the CSS display size to virtual units so changing canvas.width/height
    // for DPR does not feed back into layout and repeatedly grow the element.
    this.canvas.style.width = `${this.virtualWidth * scale}px`;
    this.canvas.style.height = `${this.virtualHeight * scale}px`;
  }

  displayBounds() {
    const viewport = this.viewportSize();
    const computedStyle = typeof getComputedStyle === 'function'
      ? getComputedStyle(this.canvas)
      : null;
    return {
      width: this.cssPixels(computedStyle?.maxWidth, viewport.width * 0.95),
      height: this.cssPixels(computedStyle?.maxHeight, viewport.height * 0.95)
    };
  }

  viewportSize() {
    const viewport = globalThis.window || {};
    return {
      width: viewport.visualViewport?.width || viewport.innerWidth || this.virtualWidth,
      height: viewport.visualViewport?.height || viewport.innerHeight || this.virtualHeight
    };
  }

  cssPixels(value, fallback) {
    const pixels = Number.parseFloat(value);
    return Number.isFinite(pixels) ? pixels : fallback;
  }

  resizeBackingStoreToDisplaySize() {
    this.syncDisplaySize();

    const { width, height } = this.canvas.getBoundingClientRect();
    const pixelRatio = (globalThis.window && globalThis.window.devicePixelRatio) || 1;
    const displayWidth = Math.max(1, Math.round(width * pixelRatio));
    const displayHeight = Math.max(1, Math.round(height * pixelRatio));

    const resized = this.canvas.width !== displayWidth || this.canvas.height !== displayHeight;
    if (resized) {
      this.canvas.width = displayWidth;
      this.canvas.height = displayHeight;
    }
    return resized;
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

  applyHudScale() {
    const pixelRatio = (globalThis.window && globalThis.window.devicePixelRatio) || 1;
    this.context.setTransform(pixelRatio, 0, 0, pixelRatio, 0, 0);
  }

  hudSize() {
    const { width, height } = this.canvas.getBoundingClientRect();
    const smallestSide = Math.min(width, height);
    return {
      width,
      height,
      fontSize: this.clamp(smallestSide * 0.04, 13, 16),
      margin: this.clamp(smallestSide * 0.025, 8, 12),
      ammoWidth: this.clamp(smallestSide * 0.012, 4, 6),
      ammoHeight: this.clamp(smallestSide * 0.036, 12, 16),
      ammoGap: this.clamp(smallestSide * 0.014, 5, 7),
      rechargeHeight: this.clamp(smallestSide * 0.006, 2, 3)
    };
  }

  clamp(value, minimum, maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  renderIfBackingSizeChanged() {
    if (this.resizeBackingStoreToDisplaySize()) {
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
    const hud = this.hudSize();

    context.font = `${hud.fontSize}px sans-serif`;
    context.textBaseline = 'top';
    context.fillStyle = 'black';
    context.fillText(
      `Distance: ${Math.floor(ship.distance)} Level: ${this.world.level} Asteroids: ${this.world.asteroidsHit}`,
      hud.margin,
      hud.margin
    );

    context.strokeStyle = 'red';
    context.lineWidth = Math.max(1, Math.round(hud.ammoWidth / 3));
    const ammoStep = hud.ammoWidth + hud.ammoGap;
    let x = hud.width - hud.margin - hud.ammoWidth;
    for (let i = 0; i < ship.ammo; i++) {
      context.strokeRect(x, hud.margin, hud.ammoWidth, hud.ammoHeight);
      x -= ammoStep;
    }

    const rechargeWidth = (hud.ammoWidth * this.world.ship.rechargeMax) / 5;
    const rechargeProgress = ship.rechargeMax - ship.rechargeRate;
    context.fillRect(
      hud.width - hud.margin - rechargeWidth,
      hud.margin / 2,
      (rechargeWidth * rechargeProgress) / ship.rechargeMax,
      hud.rechargeHeight
    );
  }

  drawPause() {
    const context = this.context;
    const hud = this.hudSize();
    const fontSize = this.clamp(hud.fontSize * 1.15, 15, 19);

    context.font = `${fontSize}px sans-serif`;
    context.textBaseline = 'middle';
    context.fillStyle = 'black';
    context.textAlign = 'center';
    const message = this.world.firstTime
      ? 'Welcome! Hold left side to move left, right side to move right, both sides to fire'
      : (this.world.gameOver ? 'Game Over: use Restart or press R' : 'Paused: use Resume or press P');
    const lines = this.wrapText(message, hud.width - (hud.margin * 2));
    const lineHeight = fontSize * 1.25;
    const startY = hud.height / 2 - ((lines.length - 1) * lineHeight) / 2;
    lines.forEach((line, index) => {
      context.fillText(line, hud.width / 2, startY + (index * lineHeight));
    });
    context.textAlign = 'left';
  }

  wrapText(text, maxWidth) {
    const context = this.context;
    const lines = [];
    let line = '';
    for (const word of text.split(' ')) {
      const nextLine = line ? `${line} ${word}` : word;
      if (line && context.measureText(nextLine).width > maxWidth) {
        lines.push(line);
        line = word;
      } else {
        line = nextLine;
      }
    }
    if (line) {
      lines.push(line);
    }
    return lines;
  }
}
