import assert from 'node:assert/strict';
import { readFile } from 'node:fs/promises';

const source = await readFile(new URL('../src/main/resources/web/escape-ship-core.js', import.meta.url), 'utf8');
const { PolygonShape } = await import(`data:text/javascript;charset=utf-8,${encodeURIComponent(source)}`);

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
  }
];

for (const test of tests) {
  test.run();
  console.log(`ok - ${test.name}`);
}
