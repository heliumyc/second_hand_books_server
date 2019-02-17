NAME = `gradle printProjectName`

VERSION = `gradle printProjectVersion`

echo $NAME
echo $VERSION
echo $PWD

echo 'run service'
java -cp ./build/libs -jar ./build/libs/$NAME-$VERSION.jar