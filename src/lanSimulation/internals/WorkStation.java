package lanSimulation.internals;


public class WorkStation extends Node {
	
	public WorkStation(String name) {
		super(name);
	}
	
	public WorkStation(String name, Node nextNode) {
		super(name, nextNode);
	}
	
	@Override
	public void printOn(Node node, StringBuffer buf) {
			buf.append("Workstation ");
			buf.append(node.name_);
			buf.append(" [Workstation]");
	}
	
	@Override
	public void printHTMLOn(StringBuffer buf, Node currentNode) {
			buf.append("Workstation ");
			buf.append(currentNode.name_);
			buf.append(" [Workstation]");
	}
	
	@Override
	public void printXMLOn(StringBuffer buf, Node currentNode) {		
			buf.append("<workstation>");
			buf.append(currentNode.name_);
			buf.append("</workstation>");
	}
}
