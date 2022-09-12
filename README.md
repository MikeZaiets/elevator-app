## Elevator application

Application without UI (console).

The buildingData consists of the nth number of floors, where n is a random number generated
at program start in the range 5 <= n <= 20.

There are k number of passengers on each floor, where k is
random number generated
at program start in the range 0 <= k <= 10.

Each passenger wants to arrive at a certain floor different from the one on which
it is located.

Each floor has two buttons to call the elevator "Up" and "Down". exception
make up the lower and upper floors.

The elevator has a capacity limit of 5 people maximum.
The first time the elevator is loaded with people from the first floor, and travels from the first (current)
to the largest of those for which passengers need.

On the way, the elevator stops at those floors where passengers need to drop off
them and picking up people who need to in the same direction in which the elevator moves.
Also, if the elevator is not fully loaded, it can stop on the floor where there is
people who need to go in the same direction.

When boarding new passengers, the maximum floor is recalculated.

