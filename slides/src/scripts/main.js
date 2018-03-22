var isWebKit = 'webkitAppearance' in document.documentElement.style,
  // zoom-based scaling causes font sizes and line heights to be calculated differently
  // on the other hand, zoom-based scaling correctly anti-aliases fonts during transforms (no need for layer creation hack)
  scaleMethod = isWebKit ? 'zoom' : 'transform',
  bespoke = require('bespoke'),
  bullets = require('bespoke-bullets'),
  classes = require('bespoke-classes'),
  fullscreen = require('bespoke-fullscreen'),
  hash = require('bespoke-hash'),
  nav = require('bespoke-nav'),
  overview = require('bespoke-overview'),
  scale = require('bespoke-scale'),
  title = require('bespoke-title'),
  onstage = require('bespoke-onstage');

bespoke.from({ parent: 'article.deck', slides: 'section' }, [
  classes(),
  nav(),
  fullscreen(),
  (scaleMethod ? scale(scaleMethod) : function(deck) {}),
  overview({ margin: 300, autostart: false, title: true, numbers: true, columns: 4 }),
  bullets('.build, .build-items > *:not(.build-items)'),
  hash(),
  onstage()
]);
