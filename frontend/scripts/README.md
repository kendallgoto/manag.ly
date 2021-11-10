CD/CI via Git is tedious because gitlab is protected under WPI VPN.

As thus, we will instead deploy via a shell script to perform an SSH copy.


Prereqs: you need to have an ~/.ssh/config with the AWS SSH credentials specified as `wpiaws`
