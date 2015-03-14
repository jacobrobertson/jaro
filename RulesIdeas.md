## Rules Engine Design ##
Jaro has a very simple rules engine that will allow new rules to be quite easily plugged in.  See for example the [PieceRules](http://code.google.com/p/jaro/source/browse/trunk/src/com/robestone/jaro/PieceRules.java) class, and the various rules implementations in the [piece rules package](http://code.google.com/p/jaro/source/browse/trunk/src/com/robestone/jaro/#jaro%2Fpiecerules).

## Considerations ##
The hardest part about coming up with rules isn't the idea, it's the graphics and the levels.  Once we have a professional looking set of graphics, it will be harder to just stick in new graphics.  And creating lots of new levels is quite a challenge.

## Some Ideas ##
  * One way doors
  * Turtles that can only be pushed in the direction they are looking
    * A “switch” of some sort that turns all turtles 90 degrees
  * Turtles that can be pushed in a row - maybe combine with above
  * bridge - can only go over, can’t go under
  * Ants - something about pushing ants all together to create a chain to an anthill.  Once all ants are connected, in one long chain that ends at the anthill, they all disappear.