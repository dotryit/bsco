module ch.feol.bsco {

   requires java.net.http;
   requires com.google.gson;

   // pi4j modules
   requires com.pi4j;
   requires com.pi4j.plugin.gpiod;

   // slf4j modules
   requires org.slf4j;
   requires org.slf4j.simple;

   uses com.pi4j.extension.Extension;
   uses com.pi4j.provider.Provider;

   // Allow access to classes in the following namespaces for pi4j annotation processing
   opens ch.feol.bsco to com.pi4j;
}
