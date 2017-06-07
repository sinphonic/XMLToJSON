package system;

import AIS.Track;
import OutputFormats.JsonMain;
import OutputFormats.MMSIVessels;

import java.lang.reflect.Type;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.json.JSONException;
import org.json.JSONML;
import org.json.JSONObject;
import org.json.JSONArray;

import Weather557.BUOY.*;
import Weather557.MESOS.*;
import Weather557.METAR.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.stanfy.gsonxml.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import utils.*;

public class MainConvertor {
	
	//[58.910 -155.666]-[56.189 -150.896]_20170601_T1000-1100.json
	private String sampleOutputFilename = "{box}_{date}_{timeRange}.json";
	private String outputFolder;
	private List<BoundingBox> boundingBoxes;
	private List<File> files = null;
	
	
	public MainConvertor() {
		boundingBoxes = new ArrayList<BoundingBox>();
		boundingBoxes.add(new BoundingBox(new Coord(58.910, -155.666), new Coord(56.189, -150.896)));
		boundingBoxes.add(new BoundingBox(new Coord(58.910, -150.896), new Coord(56.189, -146.126)));
		boundingBoxes.add(new BoundingBox(new Coord(56.189, -155.666), new Coord(53.468, -150.896)));
		boundingBoxes.add(new BoundingBox(new Coord(56.189, -150.896), new Coord(53.468, -146.126)));
		
	}
	
	public void setFileList(List<File> fileList) {
		files = fileList;
	}
	
	public void setOutputFolder(String outFolder) {
		outputFolder = (outFolder != null && !outFolder.isEmpty()) ? outFolder : ".";
	}
	
	public void convert() {
		//String xmlString = "<xml>" + "<head attrib='att' style='astYle'>" + "<title>NOAE\'s National Weather Service Real Time Mesoscale Analysis And Forecast Data</title>"+ "<field><study>meteorological</study></field></head>"+"<body><h1>HELLO WORLD</h1></body></xml>";
		try {
		
			int ii = 0;
			System.out.println("Preparing to convert " + files.size() + " files...");
			for (File f : files) {
				System.out.println("Converting file #" + ii);
				convertFile(f);
				ii++;
			}
		}
		catch (JAXBException | ParserConfigurationException | 
				IOException | SAXException | TransformerException | XPathExpressionException b) {
			System.out.println("Exception: " + b.toString());
		}
	}
	
	private void convertFile(File f) throws ParserConfigurationException, JAXBException, TransformerException, IOException, SAXException, XPathExpressionException{
		System.out.println("START CONVERT FILE");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		//System.out.println("BUILD DOC PARSE");
		Document doc = null;
		
		try {
			doc = builder.parse(f);
			//System.out.println("BUILD DOC PARSE AFTER");
			System.out.println(doc.getDocumentElement());
		} catch (SAXParseException spe) {
			System.out.println("Inserting new root and reattempting parse...");
			FileInputStream fis = new FileInputStream(f);
			List<InputStream> streams = Arrays.asList(
										new ByteArrayInputStream("<temproot>".getBytes()),
										fis, 
										new ByteArrayInputStream("</temproot>".getBytes()));
			InputStream iStream = new SequenceInputStream(Collections.enumeration(streams));
			doc = builder.parse(iStream);
			//System.out.println("AFTER REPARSE: " + doc.getDocumentElement().getChildNodes().getLength());
		}
		
		XPathFactory xFactory = XPathFactory.newInstance();
		XPath xpath = xFactory.newXPath();
		
		//Determine Root
		XPathExpression rootDeterminant = xpath.compile("/*");
		Node rootNode = (Node)rootDeterminant.evaluate(doc, XPathConstants.NODE);
		//System.out.println("ROOT IS: " + rootNode.getNodeName());
		
		
		XPathExpression expr, exprSelector;
		NodeList nl;
		Node selectorNode;
		
		if (rootNode.getNodeName().equals("temproot")) { //AIS
			exprSelector = xpath.compile("/*/*[1]");
			selectorNode = (Node)exprSelector.evaluate(doc, XPathConstants.NODE);
			switch(selectorNode.getNodeName()) {
			case "Track":
				expr = xpath.compile("//Track");
				nl = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
				
				String outputFilename = "MMSIVessels.json";
				File outputFile = new File(outputFolder, outputFilename);
				MMSIVessels vessels = new MMSIVessels();
				Gson aisGson = new Gson();
				if (!outputFile.exists()) {
					System.out.println("Output file does not exist");
				}
				else {
					System.out.println("Output file does exist");
					JsonReader jr = new JsonReader(new FileReader(outputFile));
					Type type = new TypeToken<Map<String, String>>(){}.getType();
					Map<String, String> myMap = aisGson.fromJson(jr, type);
					vessels.VESSELS = myMap;
					//vessels = aisGson.fromJson(jr, MMSIVessels.class);
				}
				for (int i = 0; i < nl.getLength(); i++) {
					System.out.println("TRACK NODE #" + (i + 1) + "/" + nl.getLength());
					Track b = (Track)getJSONObjFromXML(nl.item(i), Track.class);
					vessels.addObject(b, Track.class);
				}
				
				
				FileWriter fWriter = new FileWriter(outputFile);
				fWriter.write(aisGson.toJson(vessels.VESSELS));
				fWriter.close();
				
				
				//JsonReader jr = new JsonReader(new FileReader(outputFile));
				//jm = gson.fromJson(jr, JsonMain.class);
				//jm.addObject(jsonObj, clazz);
				
				break;
			default:
				System.out.println("Unrecognized AIS node: " + selectorNode.getNodeName() + ", skipping file: " + f.getName());
				break;
					
			}
		}
		else { //Weather 557 Data
			exprSelector = xpath.compile("/*/*/*[1]");
			selectorNode = (Node)exprSelector.evaluate(doc, XPathConstants.NODE);
			
			Gson gson = new Gson();
			switch(selectorNode.getNodeName()) {
				case "BUOY":
					expr = xpath.compile("//BUOY");
					nl = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
					for (int i = 0; i < nl.getLength(); i++) {
						Buoy b = (Buoy)getJSONObjFromXML(nl.item(i), Buoy.class);
						b.sourceRef = f.getName();
						String output = gson.toJson(b);
						writeToFile(output, 
								new Coord(Double.valueOf(b.latitude), Double.valueOf(b.longitude)), 
								b.observationDateTime, b, Buoy.class);
					}
					break;
				case "MESOS":
					expr = xpath.compile("//MESOS");
					nl = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
					for (int i = 0; i < nl.getLength(); i++) {
						Mesos m = (Mesos)getJSONObjFromXML(nl.item(i), Mesos.class);
						m.sourceRef = f.getName();
						String output = gson.toJson(m);
						writeToFile(output, 
								new Coord(Double.valueOf(m.latitude), Double.valueOf(m.longitude)), 
								m.observationDateTime, m, Mesos.class);
					}
					break;
				case "METAR":
					expr = xpath.compile("//METAR");
					nl = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
					for (int i = 0; i < nl.getLength(); i++) {
						Metar m = (Metar)getJSONObjFromXML(nl.item(i), Metar.class);
						m.sourceRef = f.getName();
						String output = gson.toJson(m);
						writeToFile(output, 
								new Coord(Double.valueOf(m.latitude), Double.valueOf(m.longitude)), 
								m.observationDateTime, m, Metar.class);
					}
					break;
				case "TRACK":
					//AIS Live Feed
					System.out.println("AIS LIVE FEED DETECTED");
					expr = xpath.compile("//TRACK");
					nl = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
					System.out.println(nl.getLength() + "roots found");
					for (int i = 0; i < nl.getLength(); i++) {
						
					}
					break;
				default:
					System.out.println("SELECTED: " + selectorNode.getNodeName() + "...Skipping file");
				break;
			}
		}
		
		
		
	}
	
	private Object getJSONObjFromXML(Node node, Class<?> clazz) throws TransformerException, JAXBException, SAXException, ParserConfigurationException, IOException {
		StringWriter writer = new StringWriter();
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty("omit-xml-declaration", "yes");
		transformer.transform(new DOMSource(node), new StreamResult(writer));
		
		String xmlString2 = writer.toString().intern();
		
		StringReader reader = new StringReader(xmlString2);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(false);
        SAXParser sp = spf.newSAXParser();
        XMLReader xmlReader = sp.getXMLReader();
        //XMLReader xRead = XMLReaderFactory.createXMLReader();
        InputSource is = new InputSource(reader);
        XMLFilter filter = new MyXMLFilter(xmlReader);
        
        
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		//UnmarshallerHandler unmarshallerHandler = unmarshaller.getUnmarshallerHandler();
        //filter.setContentHandler(unmarshallerHandler);
		/*unmarshaller.setEventHandler(
			    new ValidationEventHandler() {
			        public boolean handleEvent(ValidationEvent event ) {
			            throw new RuntimeException(event.getMessage(),
			                                       event.getLinkedException());
			        }
			});*/
		
		SAXSource source = new SAXSource(filter, is);
		
		//nf.setParent(xRead);
		//SAXSource source = new SAXSource(nf, new InputSource(reader));
		//filter.parse(is);
		//Buoy xml = (Buoy)(unmarshallerHandler.getResult());
		return unmarshaller.unmarshal(source);
	}
	
	
	
	private void writeToFile(String output, Coord coord, String dateTime, Object jsonObj, Class<?> clazz) throws IOException{
		String outputFilename = sampleOutputFilename;
		String boundingBox = null;
		BoundingBox chosenBox = null;
		String date = null;
		String time = null;
		TimeRange tr;
		
		for(BoundingBox bb : boundingBoxes) {
			if (bb.isCoordInBox(bb, coord)) {
				boundingBox = bb.toString();
				chosenBox = bb;
			}
		}
		if (boundingBox == null) {
			boundingBox = "OUTLIER";
		}
		
		date = dateTime.substring(0, dateTime.indexOf("T"));
		//time = dateTime.substring(dateTime.indexOf("T"));
		tr = new TimeRange(dateTime.substring(dateTime.indexOf("T")));
		
		
		outputFilename = outputFilename.replace("{box}", boundingBox);
		outputFilename = outputFilename.replace("{date}", date);
		outputFilename = outputFilename.replace("{timeRange}", tr.toFilenameTimeRange());
		
		//System.out.println("FOLDER: " + outputFolder);
		//System.out.println("FILENAME: " + outputFilename);
		
		File outputFile = new File(outputFolder, outputFilename);
		FileWriter fWriter;
		
		JsonMain jm = new JsonMain();
		Gson gson = new Gson();
		if (!outputFile.exists()) {
			if (chosenBox != null) {
				jm.Lat = chosenBox.getLats();
				jm.Lon = chosenBox.getLons();
			}
			else {
				jm.Lat = coord.getLat() + ""; //TODO adjust 
				jm.Lon = coord.getLon() + ""; //TODO adjust
			}
			jm.Time = tr.toJSONTimeRange();
			jm.addObject(jsonObj, clazz);
		}
		else {
			JsonReader jr = new JsonReader(new FileReader(outputFile));
			jm = gson.fromJson(jr, JsonMain.class);
			jm.addObject(jsonObj, clazz);
			//fWriter = new FileWriter(outputFile);
		}
		fWriter = new FileWriter(outputFile);
		fWriter.write(gson.toJson(jm));
		fWriter.close();
		//System.out.println("DONE");
	}
	
	private class MyXMLFilter extends XMLFilterImpl {

        public MyXMLFilter(XMLReader read) {
            super(read);
        }
        
        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) throws SAXException {
            int colonIndex = qName.indexOf(':');
            if(colonIndex >= 0) {
                qName = qName.substring(colonIndex + 1).intern();
            }
            //uri = "http://www.opengis.net/gml"; //to prevent unknown XML element exception, we have to specify the namespace here
            super.startElement("", localName, qName, attributes);
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            int colonIndex = qName.indexOf(':');
            if(colonIndex >= 0) {
                qName = qName.substring(colonIndex + 1).intern();
            }
            super.endElement(uri, localName, qName);
        }

    }
	
	
	/*private String convertNodeToJSON(Node node, String fileName) throws TransformerException, JAXBException, SAXException, ParserConfigurationException {
	StringWriter writer = new StringWriter();
	Transformer transformer = TransformerFactory.newInstance().newTransformer();
	transformer.setOutputProperty("omit-xml-declaration", "yes");
	transformer.transform(new DOMSource(node), new StreamResult(writer));
	
	String xmlString2 = writer.toString().intern();
	
	StringReader reader = new StringReader(xmlString2);
	SAXParserFactory spf = SAXParserFactory.newInstance();
	spf.setNamespaceAware(false);
    SAXParser sp = spf.newSAXParser();
    XMLReader xmlReader = sp.getXMLReader();
    //XMLReader xRead = XMLReaderFactory.createXMLReader();
    InputSource is = new InputSource(reader);
    XMLFilter filter = new MyXMLFilter(xmlReader);
    
    
    JAXBContext jaxbContext = JAXBContext.newInstance(Buoy.class);
	Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	//UnmarshallerHandler unmarshallerHandler = unmarshaller.getUnmarshallerHandler();
    //filter.setContentHandler(unmarshallerHandler);
	unmarshaller.setEventHandler(
		    new ValidationEventHandler() {
		        public boolean handleEvent(ValidationEvent event ) {
		            throw new RuntimeException(event.getMessage(),
		                                       event.getLinkedException());
		        }
		});
	
	SAXSource source = new SAXSource(filter, is);
	
	//nf.setParent(xRead);
	//SAXSource source = new SAXSource(nf, new InputSource(reader));
	//filter.parse(is);
	//Buoy xml = (Buoy)(unmarshallerHandler.getResult());
	Buoy xml = (Buoy)(unmarshaller.unmarshal(source));
	xml.sourceRef = fileName;
	Gson gson = new Gson();
	String output = gson.toJson(xml);
	return output;
	
}*/
	
	/*private void convertBUOYToJSON(Node node, String fileName) throws TransformerException, JAXBException, SAXException, ParserConfigurationException, IOException {
	StringWriter writer = new StringWriter();
	Transformer transformer = TransformerFactory.newInstance().newTransformer();
	transformer.setOutputProperty("omit-xml-declaration", "yes");
	transformer.transform(new DOMSource(node), new StreamResult(writer));
	
	String xmlString2 = writer.toString().intern();
	
	StringReader reader = new StringReader(xmlString2);
	SAXParserFactory spf = SAXParserFactory.newInstance();
	spf.setNamespaceAware(false);
    SAXParser sp = spf.newSAXParser();
    XMLReader xmlReader = sp.getXMLReader();
    //XMLReader xRead = XMLReaderFactory.createXMLReader();
    InputSource is = new InputSource(reader);
    XMLFilter filter = new MyXMLFilter(xmlReader);
    
    
    JAXBContext jaxbContext = JAXBContext.newInstance(Buoy.class);
	Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	//UnmarshallerHandler unmarshallerHandler = unmarshaller.getUnmarshallerHandler();
    //filter.setContentHandler(unmarshallerHandler);
	unmarshaller.setEventHandler(
		    new ValidationEventHandler() {
		        public boolean handleEvent(ValidationEvent event ) {
		            throw new RuntimeException(event.getMessage(),
		                                       event.getLinkedException());
		        }
		});
	
	SAXSource source = new SAXSource(filter, is);
	
	//nf.setParent(xRead);
	//SAXSource source = new SAXSource(nf, new InputSource(reader));
	//filter.parse(is);
	//Buoy xml = (Buoy)(unmarshallerHandler.getResult());
	Buoy xml = (Buoy)(unmarshaller.unmarshal(source));
	xml.sourceRef = fileName;
	Gson gson = new Gson();
	String output = gson.toJson(xml);
	writeToFile(output, 
			new Coord(Double.valueOf(xml.latitude), Double.valueOf(xml.longitude)), 
			xml.observationDateTime, xml, Buoy.class);
}*/
}


