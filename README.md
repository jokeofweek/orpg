# ORPG Engine

### Packet Structure
Packets are arrays of bytes, where the first byte is an *unsigned* byte representing the type,
followed by two bytes representing an *unsigned* short indicating the number of bytes that 
follow containing the packet contents.


