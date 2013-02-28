# ORPG Engine

## TODO
- Fix Ant build file to include library files.
- The check for segment existance should be more robust, and not just test whether the file exists.
- Make account manager keep track of who is currently using an account, and then clean up accounts when not needed (else big GC will get angry).
- Clean up API to make it more clear when requesting a segment X/Y and a map-wide X/Y
- Fix up the account ID system and then replace all the packets that use the name of the character is the ID.