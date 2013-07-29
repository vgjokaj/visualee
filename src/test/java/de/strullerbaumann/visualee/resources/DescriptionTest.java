/*
 * Created on 10.07.2013 - 16:45:45
 *
 * Copyright(c) 2013 Thomas Struller-Baumann. All Rights Reserved.
 * This software is the proprietary information of Thomas Struller-Baumann.
 */
package de.strullerbaumann.visualee.resources;

import de.strullerbaumann.visualee.examiner.JavaSourceExaminer;
import de.strullerbaumann.visualee.dependency.Dependency;
import de.strullerbaumann.visualee.dependency.DependenciyType;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Thomas Struller-Baumann <thomas at struller-baumann.de>
 */
public class DescriptionTest {

   public DescriptionTest() {
   }

   /**
    * Test of generateDescription method, of class Description.
    */
   @Test
   public void testGenerateDescription() {
      JavaSource javaSource1 = new JavaSource("TestJavaSource1");
      JavaSource javaSource2 = new JavaSource("TestJavaSource2");
      JavaSource javaSource3 = new JavaSource("TestJavaSource3");
      String testPackage = "//my/test/package/path";

      javaSource1.setPackagePath(testPackage);

      Dependency dependency12 = new Dependency(DependenciyType.INJECT, javaSource1, javaSource2);
      Dependency dependency13 = new Dependency(DependenciyType.OBSERVES, javaSource1, javaSource3);
      List<Dependency> dependencies = new ArrayList<>();
      dependencies.add(dependency12);
      dependencies.add(dependency13);
      javaSource1.setInjected(dependencies);

      String actual = Description.generateDescription(javaSource1);

      assertTrue(actual.indexOf(testPackage) > 0);
      assertTrue(actual.indexOf(javaSource2.getName()) > 0);
      assertTrue(actual.indexOf(javaSource3.getName()) > 0);
   }

   @Test
   public void testGenerateDescription2() {
      JavaSource javaSource;
      String sourceCode;

      // Produces
      javaSource = new JavaSource("Configurator");
      sourceCode = "package org.lightfish.business.configuration.boundary;\n"
              + "import org.lightfish.business.configuration.entity.Configuration;\n"
              + "import java.util.Arrays;\n"
              + "import javax.annotation.PostConstruct;\n"
              + "import javax.ejb.Singleton;\n"
              + "import javax.enterprise.inject.Produces;\n"
              + "import javax.enterprise.inject.spi.InjectionPoint;\n"
              + "import javax.ejb.Startup;\n"
              + "import javax.inject.Inject;\n"
              + "import org.lightfish.business.configuration.control.ConfigurationStore;\n"
              + "@Startup\n"
              + "@Singleton\n"
              + "public class Configurator {\n"
              + "    private Configuration configuration;\n"
              + "    @Inject ConfigurationStore configurationStore;\n"
              + "    @PostConstruct\n"
              + "    public void initialize(){\n"
              + "        this.configuration = configurationStore.retrieveConfiguration();\n"
              + "    }\n"
              + "    @Produces\n"
              + "    public int getInteger(InjectionPoint ip) {\n"
              + "        return Integer.parseInt(getString(ip));\n"
              + "    }\n"
              + "    @Produces\n"
              + "    public boolean getBoolean(InjectionPoint ip) {\n"
              + "        return Boolean.parseBoolean(getString(ip));\n"
              + "    }\n"
              + "    @Produces\n"
              + "    public String getString(InjectionPoint ip) {\n"
              + "        String name = ip.getMember().getName();\n"
              + "        return this.configuration.get(name);\n"
              + "    }\n"
              + "    @Produces\n"
              + "    public String[] getStringArray(InjectionPoint ip){\n"
              + "        return asArray(getString(ip));\n"
              + "    }\n"
              + "    public String[] getStringArray(String key){\n"
              + "        return asArray(getValue(key));\n"
              + "    }\n"
              + "    public String getValue(String key){\n"
              + "        return this.configuration.get(key);\n"
              + "    }\n"
              + "    public String[] asArray(String value){\n"
              + "       return value.split(\",\");\n"
              + "    }\n"
              + "//    public Map<String, String> getConfiguration() {\n"
              + "//        return configuration;\n"
              + "//    }\n"
              + "    public int getValueAsInt(String interval) {\n"
              + "        return Integer.parseInt(getValue(interval));\n"
              + "    }\n"
              + "    public void setValue(String key,int interval) {\n"
              + "        this.setValue(key, String.valueOf(interval));\n"
              + "    }\n"
              + "    public void setValue(String key,boolean value) {\n"
              + "        this.setValue(key, String.valueOf(value));\n"
              + "    }\n"
              + "    public void setValue(String key, String value) {\n"
              + "        this.configuration.put(key, value);\n"
              + "        configurationStore.save(configuration);\n"
              + "    }\n"
              + "    public void setArrayValue(String key, String[] values) {\n"
              + "        StringBuilder sb = new StringBuilder();\n"
              + "        for(String value:values){\n"
              + "            if(sb.length()!=0){\n"
              + "                sb.append(\",\");\n"
              + "            }\n"
              + "            sb.append(value);\n"
              + "        }\n"
              + "        this.setValue(key, String.valueOf(sb.toString()));\n"
              + "    }\n"
              + "}\n";


      javaSource.setSourceCode(sourceCode);
      JavaSourceExaminer.getInstance().findAndSetAttributes(javaSource);

      String actual = Description.generateDescription(javaSource);

      assertTrue(actual.indexOf("ConfigurationStore is injected") > 0);
      assertTrue(actual.indexOf("Produces int") > 0);
      assertTrue(actual.indexOf("Produces boolean") > 0);
      assertTrue(actual.indexOf("Produces String") > 0);
      assertTrue(actual.indexOf("Produces String[]") > 0);
   }

   /**
    * Test of getDescriptionHeader method, of class Description.
    */
   @Test
   public void testGetDescriptionHeader() {
      for (DependenciyType cdiType : DependenciyType.values()) {
         assertNotNull("No descriptionheader for CDIType " + cdiType.name(), Description.getDescriptionHeader(cdiType));
      }
   }

   /**
    * Test of getDescriptionPart method, of class Description.
    */
   @Test
   public void testGetDescriptionPart() {
      String header = "TestHeader";
      String description = "My test desciption";
      String actual = Description.getDescriptionPart(header, description);

      assertTrue(actual.indexOf(header) > 0);
      assertTrue(actual.indexOf(description) > 0);
   }
}
