A program that takes snapshot of all monitors and calculates average color values of tiles around the edge of each monitor. This information is then sent over the local network to a Raspberry Pi which controls individually addressable LED strips. The purpose of this is to create [bias lighting](https://en.wikipedia.org/wiki/Bias_lighting) around the monitors that mimics the colors on the screen.

The program creates a folder in the user's home directory called `diy-adalight`. Two JSON configuration files need to be placed in here. `config.json` and `monitor_configuration.json`. Examples of both can be found in the `examples` folder.

The server side of the project can be found [here](https://github.com/johannesmols/DIY-Adalight-Server).