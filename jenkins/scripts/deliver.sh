set -x
NAME=`gradle printProjectName`
set +x

set -x
VERSION=`gradle printProjectVersion`
set +x

echo $NAME
echo $VERSION

echo 'run service'

ls build
ls build/libs
java -cp ./build/libs/ -jar ./build/libs/$NAME-$VERSION.jar