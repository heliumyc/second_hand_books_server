echo 'get deployed'

NAME=`gradle -q printProjectName`

VERSION=`gradle -q printProjectVersion`

echo '========================================'
echo $NAME
echo $VERSION
echo '========================================'

cd /home/service
java -jar $NAME-$VERSION.jar