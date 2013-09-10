package ax.makila.comparableentitymining.utils;

import java.util.ArrayList;
import java.util.List;

public class DotUtils {
	private StringBuilder sb = new StringBuilder();
	List<DotNode> nodes = new ArrayList<DotNode>();
	
	public DotUtils() {
		sb.append("digraph G {\n");
	}
	
	public DotUtils(String fileName) {
		
	}
	
	private void testAndAddNode(DotNode node) {
		try {
			if(nodes.contains(node)) {
				throw new Exception("Node already exists!");
			}
			else {
				nodes.add(node);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public DotNode newNode(String name) {
		DotNode node = new DotNode(name);
		testAndAddNode(node);
		
		return node;
	}
	
	public void link(DotNode from, DotNode to) {
		
		
		sb.append("\t" + from + " -> " + to );
	}
	
	private void labelize() {
		for(DotNode n : nodes) {
			sb.append(n.name + " [label = \"" + n.name + "\"]");
		}
	}
	
	public void writeToFile() {
		labelize();
		sb.append("}");
	}
	
	public class DotNode {
		public String name;
		
		public DotNode(String name) {
			this.name = name;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj == null) {
				
			}
			else if(getClass() != obj.getClass()) {
				return false;
			}
			DotNode node = (DotNode) obj;
			return name.equals(node.name);
			
		}
	}
	
}
