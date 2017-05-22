# SafeDrive
Sample Integration with ZenDrive

1) Login -> scope -> SafeDriveActivity
   
   Test Access : auto@example.com:qwerty , session@example.com:qwerty , manual@example.com:qwerty

Each of these logins represent the three examples of ZenDrive - AutoTracking / SessionBased Tracking / Trip based Tracking.

Scopes are currently hardcoded and maintained in the code.

if logged into AUTO mode - Zendrive starts tracking automatically.
if logged into SESSION mode - user will be presented an option to start session.
if logged into MANUAL mode - user will be able to select the timepicker and get an alarm for a future date.

2) Zen Integration has been abstracted into module library.

