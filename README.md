# HideAndSeek
HideAndSeek is a simple Minecraft game mode with two teams, hiders and seekers, hiders need to hide from the seekers and when a seeker find a hider the hider joins the seeker team.

Known Bug
- Sometimes when a hider does not select a block before the h_countdown is over it won't actually select one for them.

Todo
- Invisible system using ProtocolLib
- Make an entity block of the hiders selected block follow them until they are in "solidified" state, 
  this state should only be broken when the player is outside of the block he was soldified on.
- Make dead hiders go in the seekers team. check // TODO in MainListeners.java
