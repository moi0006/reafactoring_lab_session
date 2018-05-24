package lanSimulation.internals;


public class Printer extends Node {

	public Printer(String name) {
		super(name);
	}

	public Printer(String name, Node nextNode) {
		super(name, nextNode);
	}
	@Override
	public void printOn(Node node, StringBuffer buf) {
			buf.append("Printer ");
			buf.append(node.name_);
			buf.append(" [Printer]");
	}
	@Override
	public void printHTMLOn(StringBuffer buf, Node currentNode) {
			buf.append("Printer ");
			buf.append(currentNode.name_);
			buf.append(" [Printer]");
	}
	@Override
	public void printXMLOn(StringBuffer buf, Node currentNode){		
			buf.append("<printer>");
			buf.append(currentNode.name_);
			buf.append("</printer>");

	}

}
