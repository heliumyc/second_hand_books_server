set -x
NAME=`gradle printProjectName`
set +x

set -x
VERSION=`gradle printProjectVersion`
set +x

echo $NAME
echo $VERSION

echo 'run service'

mkdir /home/service
cp ./build/libs/$NAME-$VERSION.jar /home/service/$NAME-$VERSION.jar
cd /home/service
java -jar $NAME-$VERSION.jar
