# Radio Plugin

simple command-based radio plugin for [Spigot](https://spigotmc.org).

## Installation

1. Download the plugin from [the release page](https://github.com/avolgha/Spigot-Radio/releases)
2. Put the plugin into the `plugins/` folder of your server
3. Restart the server

If any errors occur in this process, please reach out to me!

## Usage

In game, you have the option to execute two commands:

1. `/channel`
2. `/s`

### `/channel`-Command

With `/channel`, you can connect into an existing channel or create one yourself.
The frequency of the channel (e.g. `107.43`) has to be provided by the user:

```text
/channel 107.43
```

If you want to leave the channel, you just have to add put in `leave` instead of a frequency.

```text
/channel leave
```

The frequency follows a few simple rules:

1. Max. of 3 digits before the decimal point and a max. of 2 digits after the decimal point
2. `0 > frequency > 1000`
3. A decimal value is not required, e.g. `107` as a frequency is valid (later converted to `107.00`)

### `/s`-Command

With `/s`, you can send a message to all players in the same channel as you.
All arguments after the command name are converted into the message:

```text
/s Hallo Welt!
```

If you are not connected to any channel, an error will be sent to the player.

## Support

For support, please open an issue [here on GitHub](https://github.com/avolgha/Spigot-Radio).  
Optionally, you can as well write me on Discord: `Marius#0686`
