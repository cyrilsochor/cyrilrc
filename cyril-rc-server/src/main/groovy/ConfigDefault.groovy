music.home = "C:\\"
data.home = System.getProperty("user.home")+"\\.cyrilrc"
backup.dir = "backup"


song.suffixes = ["CDA", "MP3", "MP2", "MP1", "AAC", "VLB", "M4A", "MP4", "NSA", "FLAC", "OGG", "WMA", "MID", "MIDI", "RMI", "KAR", "MIZ", "MOD", "MDZ", "NST", "STM", "STZ", "S3M", "S3Z", "IT", "ITZ", "XM", "XMZ", "MTM", "ULT", "669", "FAR", "AMF", "OKT", "PTM", "WAV", "VOC", "AU", "SND", "AIF", "AIFF"]

player.loadCount = 20
//player.load     = ["c:/Program Files (x86)/Winamp/CLEveR.exe", "load"]
//player.loadplay = ["c:/Program Files (x86)/Winamp/CLEveR.exe", "loadplay"]

player.startup.playRandomAlbums = true

player.url = "http://localhost:4800"
player.load.url0            = "/playfile?p=pass&file=#file#" 
//player.loadplay.url0    = "/fadeoutandstop?p=pass"
player.loadplay.url0        = "/stop?p=pass"
player.loadplay.url1        = "/delete?p=pass"
player.loadplay.url2        = "/playfile?p=pass&file=#file#"
player.loadplay.url3        = "/play?p=pass"
player.currentfile.url0     = "/getplaylistfile?p=pass"
player.currentId3v1tag.url0 = "/getid3tag?p=pass&tags=t,a,y,g,l,c,r&delim=;"
player.currentId3v2tag.url0 = "/getid3v2tag?p=pass&tags=t,a,y,g,l,c,r,m,s,p,o,i,u,e,b&delim=;"
player.play.url0            = "/play?p=pass"
player.pause.url0            = "/pause?p=pass"
player.stop.url0            = "/stop?p=pass"
player.prev.url0            = "/prev?p=pass"
player.next.url0            = "/next?p=pass"
player.volumeup.cmd0		= ["nircmd.exe", "changesysvolume", "2000" ]
player.volumedown.cmd0		= ["nircmd.exe", "changesysvolume", "-2000" ]
player.changemute.cmd0		= ["nircmd.exe", "mutesysvolume", "2" ]
