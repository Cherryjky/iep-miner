package com.strongczq;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Ukkonen's linear construction of suffix tree
 * 
 * @author strongczq https://gist.github.com/strongczq/3398723
 */
public class SuffixTree {
	public static final int charSize = 128;

	public class Edge {
		private int start;
		private int end;
		private Node nextNode = null;

		public Edge(int start, int end, Node nextNode) {
			this.start = start;
			this.end = end;
			this.nextNode = nextNode;
		}

		public int length() {
			return end - start + 1;
		}

		public char getAt(int i) {
			if (i < 0 || i >= end - start + 1) {
				throw new IndexOutOfBoundsException("i = " + i
						+ ", while length = " + length());
			}
			return text[start + i];
		}

		public int getStart() {
			return start;
		}

		public void setEnd(int end) {
			this.end = end;
		}

		public int getEnd() {
			return end;
		}

		public void setNextNode(Node nextNode) {
			this.nextNode = nextNode;
		}

		public Node getNextNode() {
			return nextNode;
		}
	}

	public abstract class Node {
	}

	public class InternalNode extends Node implements Iterable<Edge> {
		private Map<Character, Edge> edges = new TreeMap<Character, Edge>();
		private InternalNode suffix = null;

		public Edge getEdge(char ch) {
			return edges.get(Character.valueOf(ch));
		}

		public void setEdge(char ch, Edge e) {
			edges.put(Character.valueOf(ch), e);
		}

		public InternalNode getSuffix() {
			return suffix;
		}

		public void setSuffix(InternalNode sNode) {
			suffix = sNode;
		}

		@Override
		public Iterator<Edge> iterator() {
			return edges.values().iterator();
		}

		public int getEdgeNum() {
			return edges.size();
		}
	}

	public class LeafNode extends Node {
		private int pos;

		public LeafNode(int pos) {
			this.pos = pos;
		}

		public int getPos() {
			return pos;
		}
	}

	// describe a position in the tree.
	private class Position {
		InternalNode node = null;
		char edgeFirstChar = (char) -1;
		int edgeIndex = 0;

		public Position() {
			node = root;
			edgeFirstChar = (char) -1;
			edgeIndex = 0;
		}

		boolean isInternalNode() {
			return edgeFirstChar == (char) -1;
		}

		InternalNode getNode() {
			return node;
		}

		// walk down stepNum steps
		public void walkDownWithOutCompare(int phase, int stepNum) {
			int count = 0;
			while (count < stepNum) {
				int pos = phase - 1 - stepNum + count;
				Edge e = node.getEdge(text[pos + 1]);
				// skip this edge
				if (e.length() <= stepNum - count) {
					node = (InternalNode) e.getNextNode();
					count += e.length();
				} else {
					edgeFirstChar = text[pos + 1];
					edgeIndex = stepNum - count - 1;
					count = stepNum;
				}
			}
		}

		// walk down one character along the tree according to the given ch
		public void walkDownOneStep(char ch) {
			if (isInternalNode()) {
				edgeFirstChar = ch;
				edgeIndex = 0;
			} else {
				edgeIndex++;
			}

			Edge cEdge = node.getEdge(edgeFirstChar);
			if (edgeIndex == cEdge.length() - 1) {
				node = (InternalNode) cEdge.getNextNode();
				edgeFirstChar = (char) -1;
				edgeIndex = 0;
			}
		}

		// return true if there is a successive position
		// according the the given character
		public boolean checkNext(char ch) {
			if (isInternalNode()) {
				return node.getEdge(ch) != null;
			} else {
				return node.getEdge(edgeFirstChar).getAt(edgeIndex + 1) == ch;
			}

		}

		// split on this current position and traverse the suffix link to the
		// next
		// node
		public void split(int phase, int extension) {
			if (isInternalNode()) {
				LeafNode lNode = new LeafNode(extension);
				node.setEdge(text[phase], new Edge(phase, text.length - 1,
						lNode));
				node = node.getSuffix();
				// suffix of root is null
				if (node == null) {
					node = root;
				}
			} else {
				Edge cEdge = node.getEdge(edgeFirstChar);
				InternalNode iNode = new InternalNode();
				Edge e1 = new Edge(cEdge.getStart() + edgeIndex + 1,
						cEdge.getEnd(), cEdge.getNextNode());
				iNode.setEdge(text[cEdge.getStart() + edgeIndex + 1], e1);
				Edge e2 = new Edge(phase, text.length - 1, new LeafNode(
						extension));
				iNode.setEdge(text[phase], e2);
				cEdge.setEnd(cEdge.getStart() + edgeIndex);
				cEdge.setNextNode(iNode);
				if (cEdge.getStart() == 0 && edgeIndex == 0) {
					iNode.setSuffix(root);
				}
				if (lastCreate != null && lastCreate.getSuffix() == null) {
					lastCreate.setSuffix(iNode);
				}
				lastCreate = iNode;
				gamma = edgeIndex + 1;
				node = node.getSuffix();
				edgeFirstChar = (char) -1;
				edgeIndex = 0;
				if (node == null) {// suffix of root is null
					node = root;
					gamma--;
				}
			}
		}

	}

	InternalNode root = null;
	// last created internal node, for preparing suffix link
	InternalNode lastCreate = null;
	// current position.
	Position current = null;
	// the length to walk down without comparing every single
	// character of the text and edge label.
	int gamma = 0;
	char[] text = null;

	private void init() {
		root = new InternalNode();
		current = new Position();
		lastCreate = null;
		gamma = 0;
	}

	public char[] getText() {
		return text;
	}

	public InternalNode getRoot() {
		return root;
	}

	/**
	 * build a suffix tree with default appended character '$'
	 * 
	 * @param input
	 */
	public void build(char[] input) {
		init();
		final char append = '$';
		this.text = new char[input.length + 1];
		System.arraycopy(input, 0, this.text, 0, input.length);
		this.text[this.text.length - 1] = append;
		int lastExtension = -1; // last Rule 1/2 extension
		for (int phase = 0; phase < text.length; ++phase) {// phases
			for (int extension = lastExtension + 1; extension <= phase; ++extension) {// extensions
				int rule = extend(phase, extension);
				if (rule == 2) {// rule 2 applied
					lastExtension = extension;
				} else {// rule 3 applied
					break;
				}
			}
		}
	}

	/**
	 * Returns all suffixes for the text that the suffix tree has been generated
	 * for
	 * 
	 * @return A list containing all the suffixes generated from the suffix tree
	 */
	@SuppressWarnings("unused")
	public List<String> getSuffix() {
		StringBuilder sb = new StringBuilder();
		walkToInternalNode(text, root, sb, "");
		String[] suffix = { "aabbabb$", "ababbabbaabbabb$", "abb$",
				"abbaabbabb$", "abbabbaabbabb$", "abbabb$", "$", "baabbabb$",
				"babb$", "babbabbaabbabb$", "babbaabbabb$", "b$", "bbaabbabb$",
				"bbabbaabbabb$", "bbabb$", "bb$" };
		Boolean check = true;
		// System.out.println("Original is ok? " + check);
		String splitString = sb.toString();
		String lines[] = splitString.split("\\r?\\n");
		// System.out.println(Arrays.toString(lines));
		return Arrays.asList(lines);
	}

	/**
	 * Walks to an internal node in the tree
	 * 
	 * @param text
	 *            The text that the tree represents
	 * @param node
	 *            The current internal node
	 * @param sb
	 *            The string builder that builds all the suffixes
	 * @param builder
	 *            Contains a string for all the previous edges in the subtree.
	 */
	private void walkToInternalNode(char[] text, InternalNode node,
			StringBuilder sb, String builder) {
		// System.out.println("Internal node");
		for (Edge edge : node) {
			walkDownEdge(text, edge, sb, builder);
		}

	}

	/**
	 * Walks to a leaf node in the tree
	 * 
	 * @param node
	 *            The current laf node
	 * @param sb
	 *            The string builder that builds all the suffixes
	 */
	private void walkToLeaf(LeafNode node, StringBuilder sb) {
		// System.out.println("Leaf " + node.getPos() + "\n______");
		sb.append('\n');
	}

	/**
	 * Walks down an edge in the tree and fetches the characters found on that
	 * edge.
	 * 
	 * @param text
	 *            The text that the tree represents
	 * @param edge
	 *            The current edge
	 * @param sb
	 *            The string builder that builds the suffixes
	 * @param builder
	 *            The previous edges of the subtree
	 */
	private void walkDownEdge(char[] text, Edge edge, StringBuilder sb,
			String builder) {
		// StringBuilder builder = new StringBuilder();
		String build = builder;
		String edgeString = "";
		for (int i = edge.getStart(); i <= edge.getEnd(); i++) {
			// if(text[i] == '$') {
			// sb.append("//0");
			// }
			// else {
			sb.append(text[i]);
			edgeString += text[i];
			// }
		}
		builder += edgeString;
		Node nextNode = edge.getNextNode();
		if (nextNode instanceof InternalNode) {
			walkToInternalNode(text, (InternalNode) nextNode, sb, builder);
		} else {
			walkToLeaf((LeafNode) nextNode, sb);
			sb.append(build);
		}
	}

	// return extension rule type, 2 or 3
	private int extend(int phase, int extension) {
		current.walkDownWithOutCompare(phase, gamma);
		gamma = 0;
		if (current.isInternalNode()) {// current position is an internal node
			if (lastCreate != null && lastCreate.getSuffix() == null) {
				lastCreate.setSuffix(current.getNode());
			}
		}
		if (!current.checkNext(text[phase])) {// Rule 2 applied
			current.split(phase, extension);
			return 2;
		} else {// Rule 3 applied
			current.walkDownOneStep(text[phase]);
			return 3;
		}
	}

}