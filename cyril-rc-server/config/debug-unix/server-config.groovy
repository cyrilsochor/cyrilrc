music.home = '/home/sochor/tmp/music-home'
//player.url = "http://192.168.23.50:4800"

//player.start    = ["winamp"]
//player.load     = ["winamp", "load"]
//player.loadplay = ["winamp", "loadplay"]
player.load.cmd0            = ["/home/sochor/.cyrilrc/play", "#file#"] 
player.loadplay.cmd0        = "rm /home/sochor/.cyrilrc/playing.txt"
player.loadplay.cmd1        = ["/home/sochor/.cyrilrc/play", "#file#"]
player.currentfile.cmd0     = "/home/sochor/.cyrilrc/current"
player.currentId3v1tag.cmd0 = "echo -n"
player.currentId3v2tag.cmd0 = "echo -n"


