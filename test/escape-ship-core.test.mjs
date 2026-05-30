import assert from 'node:assert/strict';
import { readFile } from 'node:fs/promises';

const source = await readFile(new URL('../src/main/resources/web/escape-ship-core.js', import.meta.url), 'utf8');
const { GameWorld, MAX_AMMO, PolygonShape, Ship } = await import(`data:text/javascript;charset=utf-8,${encodeURIComponent(source)}`);

function polygon(points) {
  return new PolygonShape(points);
}

const tests = [
  {
    name: 'polygon edge crossing uses function segmentsIntersect',
    run() {
      const diamond = polygon([
        { x: 0, y: 2 },
        { x: 2, y: 0 },
        { x: 4, y: 2 },
        { x: 2, y: 4 }
      ]);
      const bar = polygon([
        { x: 1, y: 1 },
        { x: 5, y: 1 },
        { x: 5, y: 3 },
        { x: 1, y: 3 }
      ]);
      assert.equal(diamond.intersects(bar), true);
    }
  },
  {
    name: 'polygon edge touching matches class PolygonShape Java collinear contact',
    run() {
      const diamond = polygon([
        { x: 0, y: 3 },
        { x: 3, y: 0 },
        { x: 6, y: 3 },
        { x: 3, y: 6 }
      ]);
      const touchingTriangle = polygon([
        { x: 1, y: 2 },
        { x: 2, y: 1 },
        { x: 0, y: 0 }
      ]);
      assert.equal(diamond.intersects(touchingTriangle), true);
    }
  },
  {
    name: 'intersectsRect detects rectangle/laser touching an asteroid edge',
    run() {
      const asteroid = polygon([
        { x: 0, y: 4 },
        { x: 4, y: 0 },
        { x: 8, y: 0 },
        { x: 10, y: 4 },
        { x: 10, y: 6 },
        { x: 8, y: 10 },
        { x: 4, y: 10 },
        { x: 0, y: 6 }
      ]);
      const laserRect = { x: 8, y: 4, width: 2, height: 2 };
      assert.equal(asteroid.intersectsRect(laserRect), true);
    }
  },
  {
    name: 'one polygon fully containing another intersects',
    run() {
      const outer = polygon([
        { x: 0, y: 0 },
        { x: 10, y: 0 },
        { x: 10, y: 10 },
        { x: 0, y: 10 }
      ]);
      const inner = polygon([
        { x: 3, y: 3 },
        { x: 7, y: 3 },
        { x: 7, y: 7 },
        { x: 3, y: 7 }
      ]);
      assert.equal(outer.intersects(inner), true);
      assert.equal(inner.intersects(outer), true);
    }
  },
  {
    name: 'ship fires one laser centered on the ship',
    run() {
      const ship = new Ship({ width: 350, height: 525 });
      const laser = ship.fire();
      assert.equal(Array.isArray(laser), false);
      assert.equal(ship.ammo, MAX_AMMO - 1);
      assert.equal(laser.width, 3);
      assert.equal(laser.x, ship.x - laser.width / 2);
    }
  },
  {
    name: 'game world adds one laser per fire action',
    run() {
      const world = new GameWorld({ width: 350, height: 525 });
      world.pause();
      world.fire();
      assert.equal(world.lasers.length, 1);
      assert.equal(world.ship.ammo, MAX_AMMO - 1);
    }
  },
  {
    name: 'game world can restart into active play',
    run() {
      const world = new GameWorld({ width: 350, height: 525 });
      world.pause();
      world.gameOver = true;
      world.paused = true;

      world.restart(true);

      assert.equal(world.gameOver, false);
      assert.equal(world.paused, false);
      assert.equal(world.firstTime, false);
    }
  },
  {
    name: 'ship carries doubled reserve ammo and recharges twice as fast',
    run() {
      const ship = new Ship({ width: 350, height: 525 }, 60);
      assert.equal(ship.ammo, 10);
      assert.equal(ship.rechargeMax, 45);

      ship.fire();
      assert.equal(ship.ammo, 9);
      for (let i = 0; i < 45; i++) {
        ship.update({ left: false, right: false });
      }
      assert.equal(ship.ammo, 10);
      assert.equal(ship.rechargeRate, 45);
    }
  }
];

for (const test of tests) {
  test.run();
  console.log(`ok - ${test.name}`);
}
