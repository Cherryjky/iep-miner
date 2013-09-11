package ax.makila.comparableentitymining.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Class for writing dot files. Creates a digraph and allows for linking of nodes.
 * The nodes are identified by their ID and this class automatically labels them
 * according to their label. 
 * @author fredrik
 */
public class DotWriter {
	//Builds the string for the output file
	private StringBuilder sb = new StringBuilder();
	//Contains all the nodes
	private List<DotNode> nodes = new ArrayList<DotNode>();
	//The default file name
	private String fileName = "out";
	//The file suffix to be appended
	private final String fileSuffix = ".gv";
	//The output file
	private File file;
	//A counter to give each node a unique ID
	private AtomicInteger idCounter = new AtomicInteger();
	
	/**
	 * Constructor. Uses the default fileName "out.gv" as the output file
	 */
	public DotWriter() {
		file = new File(fileName + fileSuffix);
		sb.append("digraph G {\n");
	}
	
	/**
	 * Constructor. Allows for specification of the fileName to be used. Adding
	 * the suffix ".gv" is not necessary as it is appended automatically.
	 * @param fileName The name of the output file
	 */
	public DotWriter(String fileName) {
		this.fileName = fileName;
		file = new File(fileName + fileSuffix);
		sb.append("digraph G {\n");
	}
	
	/**
	 * Adds a new node to the list of nodes, but only if this node doesn't already exist
	 * @param node The node to be added to the list of nodes
	 */
	private void testAndAddNode(DotNode node) {
		try {
			if(nodes.contains(node)) {;
				throw new Exception("Node with id " + node.id + " already exists!");
			}
			else {
				nodes.add(node);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Creates and returns a new node with the label <tt>label</tt> and adds it to
	 * the list of nodes.
	 * @param label The label of the new node 
	 * @return The created node
	 */
	public DotNode newNode(String label) {
		DotNode node = new DotNode(label);
		testAndAddNode(node);
		
		return node;
	}
	
	/**
	 * Creates a link from node <tt>from</tt> to node <tt>to</tt> by appending a string
	 * of the form "from -> to" to the output string;
	 * @param from The departure node
	 * @param to The destination node
	 */
	public void link(DotNode from, DotNode to) {
		sb.append(from.id + " -> " + to.id + "\n");
	}
	
	/**
	 * Labels all the nodes in the list of nodes so the resulting graphical 
	 * representation of the output file will show the nodes' labels instead of its
	 * ID. The string is of the form "node [label = "label"]" and is added to the
	 * output string
	 */
	private void labelize() {
		for(DotNode n : nodes) {
			sb.append(n.id + " [label = \"" + n.label + "\"]\n");
		}
	}
	
	/**
	 * Finalizes the output and writes the result to the file. Also closes the
	 * output stream. Should be the last call to the DotWriter object.
	 */
	public void finalize() {
		labelize();
		sb.append("}");
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sb.toString());
			bw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Class for creating nodes in the graph. The constructor is private and should
	 * only be accessed by DotWriter.newNode(String)
	 * @author fredrik
	 */
	public class DotNode {
		/**
		 * The label of the node
		 */
		public String label;
		/**
		 * The unique ID of the node
		 */
		public int id;
		
		/**
		 * Constructor. Adds a unique label to each node created. Private as it is
		 * only accessed through DotWriter.newNode(String) 
		 * @param label The label of the node
		 */
		private DotNode(String label) {
			this.label = label;
			id = idCounter.getAndIncrement();
		}
		
		@Override
		public int hashCode() {
	        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
	            // if deriving: appendSuper(super.hashCode()).
	            append(label).
	            append(id).
	            toHashCode();
	    }

		@Override
		public boolean equals(Object obj) {
			if(obj == null) {
				
			}
			else if(getClass() != obj.getClass()) {
				return false;
			}
			DotNode node = (DotNode) obj;
			
	        return new EqualsBuilder().
	            append(label, node.label).
	            append(id, node.id).
	            isEquals();
			
		}
	}
	
}
