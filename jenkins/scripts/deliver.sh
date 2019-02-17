set -x
NAME=`gradle printProjectName`
set +x

set -x
VERSION=`gradle printProjectVersion`
set +x

echo $NAME
echo $VERSION

echo 'run service'
cd ./build/libs
java -jar $NAME-$VERSION.jar
# java -cp ./build/libs/ -jar ./build/libs/$NAME-$VERSION.jar