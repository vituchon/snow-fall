compile

`javac $(find . -iname '*.java' -printf '%p ')`

run

`java main.Main 60 800,800`

60 = FPS
800,800 = Canvas size