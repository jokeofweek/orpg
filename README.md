# ORPG Engine

## TODO
- The check for segment existance should be more robust, and not just test whether the file exists.
- Clean up API to make it more clear when requesting a segment X/Y and a map-wide X/Y
- Fix up the account ID system and then replace all the packets that use the name of the character is the ID.
- Implement autotiling.
- Fix seamless scroll to determine which segments are need first perhaps?
- On changing a map, the viewbox needs to be rebuilt with the right content width /content height

## DONE

- "Fix Ant build file to include library files." - Had to include .jar files using zipgroupfileset.
- "Make account manager keep track of who is currently using an account, and then clean up accounts when not needed (else big GC will get angry)." - Fixed by having the session manager have a hashmap of lists of sessions per account name. Whenever a session disconnects, we remove the session from the list for the given account in the hash map. If the list is empty, we notify the account controller that it can release the account.
- Scroll client map and request segments as you go.
- "Modify the NEED_SEGMENT packets to send over local revision." - The local segments are now loaded on a local Map object and the revision/revisionTime are sent along with the need segment. The ClientNeedSegmentHandler then checks with the server revision data, and passes it to the ClientSegmentDataPacket. A boolean is sent in the segment data packet stating whether the revisions match. If the revisions do not match, then it sends the segment data. Finally it sends the players in both cases.

## Resources
- [Autotile](http://blog.rpgmakerweb.com/tutorials/anatomy-of-an-autotile/)
