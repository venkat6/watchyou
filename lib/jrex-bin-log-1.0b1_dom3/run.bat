SET JAVA_HOME="C:\Program Files\Java\jdk1.6.0_18\bin\java"
SET JREX_GRE_PATH=C:/jrex_gre
%JAVA_HOME% -cp ./JRex.jar -Djrex.dom.enable=true -Djrex.gre.path=%JREX_GRE_PATH% -DJREX_DEBUG=true test.JRexExample
PAUSE>NUL