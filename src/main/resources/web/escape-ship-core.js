export const DEFAULT_UPDATES_PER_SECOND = 60;
export const MAX_AMMO = 10;

export class PolygonShape {
  constructor(points) {
    this.points = points;
  }

  bounds() {
    const xs = this.points.map(point => point.x);
    const ys = this.points.map(point => point.y);
    const minX = Math.min(...xs);
    const minY = Math.min(...ys);
    return { x: minX, y: minY, width: Math.max(...xs) - minX, height: Math.max(...ys) - minY };
  }

  intersectsRect(rect) {
    const rectangle = new PolygonShape([
      { x: rect.x, y: rect.y },
      { x: rect.x + rect.width, y: rect.y },
      { x: rect.x + rect.width, y: rect.y + rect.height },
      { x: rect.x, y: rect.y + rect.height }
    ]);
    return rectsIntersect(this.bounds(), rect) && this.intersects(rectangle);
  }

  intersects(other) {
    if (!rectsIntersect(this.bounds(), other.bounds())) {
      return false;
    }

    for (let i = 0; i < this.points.length; i++) {
      const a1 = this.points[i];
      const a2 = this.points[(i + 1) % this.points.length];
      for (let j = 0; j < other.points.length; j++) {
        const b1 = other.points[j];
        const b2 = other.points[(j + 1) % other.points.length];
        if (segmentsIntersect(a1, a2, b1, b2)) {
          return true;
        }
      }
    }
    return pointInPolygon(other.points[0], this.points) || pointInPolygon(this.points[0], other.points);
  }
}

export class Ship {
  constructor(size, updatesPerSecond = DEFAULT_UPDATES_PER_SECOND) {
    this.size = size;
    this.updatesPerSecond = updatesPerSecond;
    this.rechargeMax = Math.floor(0.75 * updatesPerSecond);
    this.reset();
  }

  reset() {
    this.travelSpeed = 60 / this.updatesPerSecond;
    this.speed = this.size.width / (this.updatesPerSecond * 2);
    this.distance = 0;
    this.ammo = MAX_AMMO;
    this.rechargeRate = this.rechargeMax;
    this.y = this.size.height * 14.8 / 16;
    this.x = this.size.width / 2;
  }

  fire() {
    this.ammo--;
    return new Laser(this.x, this.y, this.size, this.updatesPerSecond);
  }

  update(input) {
    this.distance += this.travelSpeed;
    const halfWidth = this.size.width / (350 / 6);
    if (input.left && !input.right && this.x > halfWidth) {
      this.x -= this.speed;
    } else if (input.right && !input.left && this.x < this.size.width - halfWidth) {
      this.x += this.speed;
    }

    if (this.ammo !== MAX_AMMO && --this.rechargeRate === 0) {
      this.rechargeRate = this.rechargeMax;
      this.ammo++;
    }
  }

  updateSpeed(gameSpeed) {
    if (this.travelSpeed < 2) {
      this.travelSpeed *= gameSpeed;
    }
    if (this.speed < 3) {
      this.speed *= gameSpeed;
    }
  }

  shape() {
    const x = this.x;
    const y = this.y;
    const width = this.size.width;
    const height = this.size.height;
    const halfWidth = width / (350 / 6);
    const innerWidth = width / (350 / 2.4);
    const noseHeight = height / (525 / 13);
    const lowerShoulder = height / (525 / 8);
    const tailHeight = height / (525 / 4.5);
    const tailNotch = height / (525 / 5.8);
    return new PolygonShape([
      { x, y: y - noseHeight },
      { x: x + halfWidth, y: y + lowerShoulder },
      { x: x + innerWidth, y: y + tailNotch },
      { x, y: y + tailHeight },
      { x: x - innerWidth, y: y + tailNotch },
      { x: x - halfWidth, y: y + lowerShoulder }
    ]);
  }
}

export class Laser {
  constructor(shipX, shipY, size, updatesPerSecond = DEFAULT_UPDATES_PER_SECOND) {
    this.size = size;
    this.width = size.width / (350 / 3);
    this.height = size.height / (525 / 9);
    this.x = shipX - this.width / 2;
    this.y = shipY - size.height / (525 / 13);
    this.ySpeed = size.height / (updatesPerSecond * 1.75);
  }

  update() {
    this.y -= this.ySpeed;
  }

  offscreen() {
    return this.y + this.height < 0;
  }

  rect() {
    return { x: this.x, y: this.y, width: this.width, height: this.height };
  }
}

export class Asteroid {
  constructor(size, speedModifier, updatesPerSecond = DEFAULT_UPDATES_PER_SECOND, random = Math.random) {
    this.gameSize = size;
    const base = Math.floor(random() * 10) + 10;
    this.size = Math.floor(size.width / (350 / base));
    const topX = Math.floor(random() * (size.width + 20)) - 9;
    const botX = Math.floor(random() * size.width) - 1;
    this.x = topX;
    this.y = -10;
    const modifier = speedModifier + ((Math.floor(random() * 30) - 15) / 100);
    this.xSpeed = ((botX - topX) / (updatesPerSecond * 3.5)) * modifier;
    this.ySpeed = (size.height / (updatesPerSecond * 3.5)) * modifier;
  }

  update() {
    this.y += this.ySpeed;
    this.x += this.xSpeed;
  }

  offscreen() {
    return this.x < -20 || this.x > this.gameSize.width + 20 || this.y > this.gameSize.height + 20;
  }

  shape() {
    const x = this.x;
    const y = this.y;
    const size = this.size;
    return new PolygonShape([
      { x: x - size / 2, y: y - size / 5 },
      { x: x - size / 5, y: y - size / 2 },
      { x: x + size / 5, y: y - size / 2 },
      { x: x + size / 2, y: y - size / 5 },
      { x: x + size / 2, y: y + size / 5 },
      { x: x + size / 5, y: y + size / 2 },
      { x: x - size / 5, y: y + size / 2 },
      { x: x - size / 2, y: y + size / 5 }
    ]);
  }
}

export class GameWorld {
  constructor(size, updatesPerSecond = DEFAULT_UPDATES_PER_SECOND, random = Math.random) {
    this.size = size;
    this.updatesPerSecond = updatesPerSecond;
    this.random = random;
    this.ship = new Ship(size, updatesPerSecond);
    this.input = { left: false, right: false };
    this.start();
  }

  start() {
    this.level = 1;
    this.gameSpeed = 1;
    this.asteroidWait = 0;
    this.lasers = [];
    this.asteroids = [];
    this.paused = true;
    this.gameOver = false;
    this.firstTime = true;
    this.asteroidsHit = 0;
    this.distanceTillNextLevel = 1000;
    this.previousDistanceTillNextLevel = 0;
    this.waitMax = 1.5;
    this.waitMin = 1;
    this.ship.reset();
  }

  pause() {
    if (!this.paused && !this.gameOver) {
      this.paused = true;
    } else if (this.paused && !this.gameOver) {
      this.paused = false;
      this.firstTime = false;
    }
  }

  restart(startImmediately = false) {
    if (this.paused) {
      this.start();
      if (startImmediately) {
        this.pause();
      }
    }
  }

  fire() {
    if (!this.paused && this.ship.ammo > 0) {
      this.lasers.push(this.ship.fire());
    }
  }

  update() {
    if (this.paused) {
      return;
    }

    this.spawnAsteroidsIfNeeded();
    this.asteroidWait--;
    this.updateLasers();
    this.updateAsteroids();
    this.advanceLevelIfNeeded();
    this.ship.update(this.input);
  }

  spawnAsteroidsIfNeeded() {
    if (this.asteroids.length < (this.level * Math.sqrt(this.level) + 4) / 2) {
      this.asteroids.push(new Asteroid(this.size, this.gameSpeed, this.updatesPerSecond, this.random));
    } else if (this.asteroidWait <= 0) {
      this.asteroidWait = Math.floor((this.updatesPerSecond / 3) * (this.waitMin + (this.random() * ((this.waitMax - this.waitMin) + 1))));
      this.asteroids.push(new Asteroid(this.size, this.gameSpeed, this.updatesPerSecond, this.random));
    }
  }

  updateLasers() {
    for (let i = this.lasers.length - 1; i >= 0; i--) {
      const laser = this.lasers[i];
      laser.update();
      let hit = false;
      for (let j = this.asteroids.length - 1; j >= 0; j--) {
        if (this.asteroids[j].shape().intersectsRect(laser.rect())) {
          this.asteroids.splice(j, 1);
          hit = true;
          this.asteroidsHit++;
        }
      }
      if (hit || laser.offscreen()) {
        this.lasers.splice(i, 1);
      }
    }
  }

  updateAsteroids() {
    for (let i = this.asteroids.length - 1; i >= 0; i--) {
      const asteroid = this.asteroids[i];
      asteroid.update();
      if (asteroid.offscreen()) {
        this.asteroids.splice(i, 1);
      } else if (asteroid.shape().intersects(this.ship.shape())) {
        this.gameOver = true;
        this.paused = true;
      }
    }
  }

  advanceLevelIfNeeded() {
    if (this.ship.distance - this.previousDistanceTillNextLevel <= this.distanceTillNextLevel) {
      return;
    }
    this.previousDistanceTillNextLevel = this.distanceTillNextLevel;
    this.level++;
    this.waitMax = this.waitMin / 2;
    this.waitMin = (1 / this.level) * (1 / this.level);
    this.gameSpeed = (Math.log(this.level) / 2) + 1;
    this.ship.updateSpeed(this.gameSpeed);
    this.distanceTillNextLevel *= this.gameSpeed;
  }
}

function rectsIntersect(a, b) {
  return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
}

function pointInPolygon(point, points) {
  let inside = false;
  for (let i = 0, j = points.length - 1; i < points.length; j = i++) {
    const pi = points[i];
    const pj = points[j];
    if (((pi.y > point.y) !== (pj.y > point.y)) && (point.x < (pj.x - pi.x) * (point.y - pi.y) / (pj.y - pi.y) + pi.x)) {
      inside = !inside;
    }
  }
  return inside;
}

function segmentsIntersect(p1, p2, q1, q2) {
  const d1 = direction(q1, q2, p1);
  const d2 = direction(q1, q2, p2);
  const d3 = direction(p1, p2, q1);
  const d4 = direction(p1, p2, q2);
  return (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0)))
    || (d1 === 0 && onSegment(q1, q2, p1))
    || (d2 === 0 && onSegment(q1, q2, p2))
    || (d3 === 0 && onSegment(p1, p2, q1))
    || (d4 === 0 && onSegment(p1, p2, q2));
}

function direction(a, b, c) {
  return ((c.x - a.x) * (b.y - a.y)) - ((b.x - a.x) * (c.y - a.y));
}

function onSegment(a, b, c) {
  return Math.min(a.x, b.x) <= c.x
    && c.x <= Math.max(a.x, b.x)
    && Math.min(a.y, b.y) <= c.y
    && c.y <= Math.max(a.y, b.y);
}
