This is a placeholder for the RayscodeLexer generated file.

To generate this file, do the following:
1. Download [JFlex](http://jflex.de/download.html). Make sure the files in {JFlex Directory}/lib are in your classpath.
2. Use the Flex task in Ant to generate the parser. The task will automatically parse out rayscode.flex in the
resources/ file, and place it in lucbui.rayscode.lexer. This can then be compiled and used.

More detailed instructions, for users of IntelliJ:
1. Download JFlex, as above.
2. Go to View > Tool Windows > Ant Build. An empty side window will appear.
3. Click on the +, and select the rayscode.xml file. Alternatively, drag and drop the rayscode.xml
file from the Project View to the empty window. Tasks will automatically populate.
4. Click the Properties button in the Ant Build window. Select the "Additional Classpath" tab. Click add,
and select the two jars in your JFlex /lib/ file. 
5. To run the Ant build, double-click the "flex" tab. The lexer file will be parsed from /resources/rayscode.flex,
and insert into the file this readme is located.