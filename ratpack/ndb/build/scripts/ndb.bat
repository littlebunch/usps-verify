@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  ndb startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and NDB_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%/app;%APP_HOME%\lib\ndb.jar;%APP_HOME%\lib\ratpack-core-1.4.1.jar;%APP_HOME%\lib\ratpack-groovy-1.4.1.jar;%APP_HOME%\lib\ratpack-hikari-1.4.1.jar;%APP_HOME%\lib\mariadb-java-client-1.5.2.jar;%APP_HOME%\lib\grails-spring-3.0.9.jar;%APP_HOME%\lib\grails-datastore-gorm-hibernate4-5.0.10.RELEASE.jar;%APP_HOME%\lib\slf4j-simple-1.7.21.jar;%APP_HOME%\lib\netty-codec-http-4.1.4.Final.jar;%APP_HOME%\lib\netty-handler-4.1.4.Final.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.4.Final-linux-x86_64.jar;%APP_HOME%\lib\guava-19.0.jar;%APP_HOME%\lib\slf4j-api-1.7.21.jar;%APP_HOME%\lib\reactive-streams-1.0.0.jar;%APP_HOME%\lib\caffeine-2.3.1.jar;%APP_HOME%\lib\javassist-3.19.0-GA.jar;%APP_HOME%\lib\jackson-databind-2.7.5.jar;%APP_HOME%\lib\jackson-dataformat-yaml-2.7.5.jar;%APP_HOME%\lib\jackson-datatype-guava-2.7.5.jar;%APP_HOME%\lib\snakeyaml-1.15.jar;%APP_HOME%\lib\jackson-datatype-jdk8-2.7.5.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.7.5.jar;%APP_HOME%\lib\groovy-all-2.4.3.jar;%APP_HOME%\lib\ratpack-guice-1.4.1.jar;%APP_HOME%\lib\HikariCP-2.3.5.jar;%APP_HOME%\lib\spring-web-4.1.7.RELEASE.jar;%APP_HOME%\lib\jcl-over-slf4j-1.7.10.jar;%APP_HOME%\lib\hibernate-core-4.3.10.Final.jar;%APP_HOME%\lib\hibernate-validator-5.0.3.Final.jar;%APP_HOME%\lib\grails-datastore-gorm-hibernate-core-5.0.10.RELEASE.jar;%APP_HOME%\lib\groovy-2.4.5.jar;%APP_HOME%\lib\hibernate-commons-annotations-4.0.5.Final.jar;%APP_HOME%\lib\dom4j-1.6.1.jar;%APP_HOME%\lib\grails-datastore-gorm-support-5.0.10.RELEASE.jar;%APP_HOME%\lib\netty-codec-4.1.4.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.4.Final.jar;%APP_HOME%\lib\netty-transport-4.1.4.Final.jar;%APP_HOME%\lib\netty-common-4.1.4.Final.jar;%APP_HOME%\lib\jackson-annotations-2.7.0.jar;%APP_HOME%\lib\jackson-core-2.7.5.jar;%APP_HOME%\lib\guice-4.1.0.jar;%APP_HOME%\lib\guice-multibindings-4.1.0.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\groovy-ant-2.4.5.jar;%APP_HOME%\lib\groovy-xml-2.4.5.jar;%APP_HOME%\lib\spring-aop-4.1.7.RELEASE.jar;%APP_HOME%\lib\spring-context-4.1.7.RELEASE.jar;%APP_HOME%\lib\jboss-logging-3.1.3.GA.jar;%APP_HOME%\lib\jboss-logging-annotations-1.2.0.Beta1.jar;%APP_HOME%\lib\jboss-transaction-api_1.2_spec-1.0.0.Final.jar;%APP_HOME%\lib\hibernate-jpa-2.1-api-1.0.0.Final.jar;%APP_HOME%\lib\antlr-2.7.7.jar;%APP_HOME%\lib\jandex-1.1.0.Final.jar;%APP_HOME%\lib\validation-api-1.1.0.Final.jar;%APP_HOME%\lib\classmate-1.0.0.jar;%APP_HOME%\lib\jta-1.1.jar;%APP_HOME%\lib\grails-datastore-gorm-5.0.10.RELEASE.jar;%APP_HOME%\lib\spring-orm-4.1.7.RELEASE.jar;%APP_HOME%\lib\grails-datastore-core-5.0.10.RELEASE.jar;%APP_HOME%\lib\spring-jdbc-4.1.7.RELEASE.jar;%APP_HOME%\lib\grails-async-3.0.12.jar;%APP_HOME%\lib\grails-core-3.0.12.jar;%APP_HOME%\lib\netty-resolver-4.1.4.Final.jar;%APP_HOME%\lib\aopalliance-1.0.jar;%APP_HOME%\lib\spring-expression-4.1.7.RELEASE.jar;%APP_HOME%\lib\grails-validation-3.0.12.jar;%APP_HOME%\lib\concurrentlinkedhashmap-lru-1.3.1.jar;%APP_HOME%\lib\gpars-1.2.1.jar;%APP_HOME%\lib\reactor-stream-2.0.6.RELEASE.jar;%APP_HOME%\lib\reactor-core-2.0.6.RELEASE.jar;%APP_HOME%\lib\spring-boot-1.2.7.RELEASE.jar;%APP_HOME%\lib\commons-validator-1.4.1.jar;%APP_HOME%\lib\jsr166y-1.7.0.jar;%APP_HOME%\lib\commons-collections-3.2.1.jar;%APP_HOME%\lib\grails-bootstrap-3.0.12.jar;%APP_HOME%\lib\groovy-groovydoc-2.4.5.jar;%APP_HOME%\lib\ant-antlr-1.9.4.jar;%APP_HOME%\lib\groovy-templates-2.4.5.jar;%APP_HOME%\lib\spring-core-4.1.8.RELEASE.jar;%APP_HOME%\lib\spring-tx-4.1.8.RELEASE.jar;%APP_HOME%\lib\spring-beans-4.1.8.RELEASE.jar
cd "%APP_HOME%/app"

@rem Execute ndb
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %NDB_OPTS%  -classpath "%CLASSPATH%" ratpack.groovy.GroovyRatpackMain %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable NDB_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%NDB_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
