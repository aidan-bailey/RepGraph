package com.repgraph.repgraph;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import com.repgraph.models.Graph;
import com.repgraph.models.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RepgraphGraphPropertiesTests {

	DMRSLibrary lib = new DMRSLibrary("src/test/resources/testCompare.dmrs");

	@Test
	void testConnected(){
		//Nodes for graph
		Node n1 = new Node(0);
		Node n2 = new Node(1);
		Node n3 = new Node(2);
		Node n4 = new Node(3);
		Node n5 = new Node(4);
		Node n6 = new Node(5);
		Node n7 = new Node(6);

		//Edges
		Edge one3 = new Edge(n3);  
		Edge two3 = new Edge(n3);  
		Edge four3 = new Edge(n3); 
		Edge two1 = new Edge(n1);  
		Edge five1 = new Edge(n1); 
		Edge six2 = new Edge(n2);  
		Edge seven6 = new Edge(n6);
		Edge seven5 = new Edge(n5);
		Edge five4 = new Edge(n4); 

		//Adding adjacent edges to nodes
		n1.addAdjacent(n2); 
		n1.addAdjacent(n5); 
		n2.addAdjacent(n6); 
		n3.addAdjacent(n1); 
		n3.addAdjacent(n2); 
		n3.addAdjacent(n4); 
		n4.addAdjacent(n5); 
		n5.addAdjacent(n7); 
		n6.addAdjacent(n7);
		
		//Adding edges to nodes
		n1.addEdge(one3);  
		n2.addEdge(two3);  
		n2.addEdge(two1);  
		n4.addEdge(four3); 
		n5.addEdge(five1); 
		n5.addEdge(five4); 
		n6.addEdge(six2);  
		n7.addEdge(seven5);
		n7.addEdge(seven6);

		ArrayList<Node> nodes = new ArrayList<Node>(Arrays.asList(n1, n2, n3, n4, n5, n6, n7));
		Graph testingGraph = new Graph(nodes);
		Properties pTest = new Properties(testingGraph);
		assertTrue(pTest.getConnected());
	}

	@Test
	void testNotConnected(){
		//Nodes for graph
		Node n1 = new Node(0);
		Node n2 = new Node(1);
		Node n3 = new Node(2);
		Node n4 = new Node(3);
		Node n5 = new Node(4);
		Node n6 = new Node(5);
		Node n7 = new Node(6);

		//Edges 
		Edge two3 = new Edge(n3);  
		Edge four3 = new Edge(n3);   
		Edge five1 = new Edge(n1);   
		Edge seven6 = new Edge(n6);
		Edge seven5 = new Edge(n5); 

		//Adding adjacent edges to nodes 
		n1.addAdjacent(n5); 
		n3.addAdjacent(n2); 
		n3.addAdjacent(n4); 
		n5.addAdjacent(n7); 
		n6.addAdjacent(n7);
		
		//Adding edges to nodes
		n2.addEdge(two3);    
		n4.addEdge(four3); 
		n5.addEdge(five1);   
		n7.addEdge(seven5);
		n7.addEdge(seven6);

		ArrayList<Node> nodes = new ArrayList<Node>(Arrays.asList(n1, n2, n3, n4, n5, n6, n7));
		Graph testingGraph = new Graph(nodes);
		Properties pTest = new Properties(testingGraph);
		assertFalse(pTest.getConnected());
	}

	@Test
	void longestPathConnected(){
		//Nodes for graph
		Node n1 = new Node(0);
		Node n2 = new Node(1);
		Node n3 = new Node(2);
		Node n4 = new Node(3);
		Node n5 = new Node(4);
		Node n6 = new Node(5);
		Node n7 = new Node(6);

		//Edges
		Edge one3 = new Edge(n3);  
		Edge two3 = new Edge(n3);  
		Edge four3 = new Edge(n3); 
		Edge two1 = new Edge(n1);  
		Edge five1 = new Edge(n1); 
		Edge six2 = new Edge(n2);  
		Edge seven6 = new Edge(n6);
		Edge seven5 = new Edge(n5);
		Edge five4 = new Edge(n4); 

		//Adding adjacent edges to nodes
		n1.addAdjacent(n2); 
		n1.addAdjacent(n5); 
		n2.addAdjacent(n6); 
		n3.addAdjacent(n1); 
		n3.addAdjacent(n2); 
		n3.addAdjacent(n4); 
		n4.addAdjacent(n5); 
		n5.addAdjacent(n7); 
		n6.addAdjacent(n7);
		
		//Adding edges to nodes
		n1.addEdge(one3);  
		n2.addEdge(two3);  
		n2.addEdge(two1);  
		n4.addEdge(four3); 
		n5.addEdge(five1); 
		n5.addEdge(five4); 
		n6.addEdge(six2);  
		n7.addEdge(seven5);
		n7.addEdge(seven6);

		ArrayList<Node> nodes = new ArrayList<Node>(Arrays.asList(n1, n2, n3, n4, n5, n6, n7));
		Graph testingGraph = new Graph(nodes);
		Properties pTest = new Properties(testingGraph);
		//Assertions
		assertEquals(4, pTest.getLongestPath());
		assertTrue(n7.getLableMatch());
		assertTrue(n6.getLableMatch());
		assertTrue(n2.getLableMatch());
		assertTrue(n1.getLableMatch());
		assertTrue(n3.getLableMatch());
		assertFalse(n4.getLableMatch());
		assertFalse(n5.getLableMatch());
	}

	@Test
	void longestPathNotConnected(){
		//Nodes for graph
		Node n1 = new Node(0);
		Node n2 = new Node(1);
		Node n3 = new Node(2);
		Node n4 = new Node(3);
		Node n5 = new Node(4);
		Node n6 = new Node(5);
		Node n7 = new Node(6);

		//Edges 
		Edge two3 = new Edge(n3);  
		Edge four3 = new Edge(n3);   
		Edge five1 = new Edge(n1);   
		Edge seven6 = new Edge(n6);
		Edge seven5 = new Edge(n5); 

		//Adding adjacent edges to nodes 
		n1.addAdjacent(n5); 
		n3.addAdjacent(n2); 
		n3.addAdjacent(n4); 
		n5.addAdjacent(n7); 
		n6.addAdjacent(n7);
		
		//Adding edges to nodes
		n2.addEdge(two3);    
		n4.addEdge(four3); 
		n5.addEdge(five1);   
		n7.addEdge(seven5);
		n7.addEdge(seven6);

		ArrayList<Node> nodes = new ArrayList<Node>(Arrays.asList(n1, n2, n3, n4, n5, n6, n7));
		Graph testingGraph = new Graph(nodes);
		Properties pTest = new Properties(testingGraph);
		//assertions
		assertEquals(2, pTest.getLongestPath());
		assertTrue(n7.getLableMatch());
		assertTrue(n5.getLableMatch());
		assertTrue(n1.getLableMatch());
		assertFalse(n6.getLableMatch());
		assertFalse(n2.getLableMatch());
		assertFalse(n3.getLableMatch());
		assertFalse(n4.getLableMatch());
	}

	@Test
	void longestPathTwoPaths(){
		//Nodes for graph
		Node n1 = new Node(0);
		Node n2 = new Node(1);
		Node n3 = new Node(2);
		Node n4 = new Node(3);

		//Edges 
		Edge e13 = new Edge(n3);
		Edge e12 = new Edge(n2);
		Edge e34 = new Edge(n4);
		Edge e24 = new Edge(n4); 

		//Adding adjacent edges to nodes 
		n2.addAdjacent(n1);
		n3.addAdjacent(n1);
		n4.addAdjacent(n2);
		n4.addAdjacent(n3);
		
		//Adding edges to nodes
		n1.addEdge(e13);
		n1.addEdge(e12);
		n2.addEdge(e24);
		n3.addEdge(e34);

		ArrayList<Node> nodes = new ArrayList<Node>(Arrays.asList(n1, n2, n3, n4));
		Graph testingGraph = new Graph(nodes);
		Properties pTest = new Properties(testingGraph);
		//assertions
		assertEquals(2, pTest.getLongestPath());
		assertTrue(n1.getLableMatch());
		assertTrue(n2.getLableMatch());
		assertTrue(n4.getLableMatch());
		assertFalse(n3.getLableMatch());
	}

	@Test
	void Planar(){
		//make tokens
		Token t0 = new Token(0);                                                  
		Token t1 = new Token(1);                                                  
		Token t2 = new Token(2);                                                  
		Token t3 = new Token(3);                                                  
		Token t4 = new Token(4);                                                  
		Token t5 = new Token(5);                                                  
			
		//make nodes
		Node n0 = new Node(0);                                                    
		Node n1 = new Node(1);                                                    
		Node n2 = new Node(2);                                                    
		Node n3 = new Node(3);                                                    
		Node n4 = new Node(4);                                                    
		Node n5 = new Node(5);                                                    
		  
		//add tokens to nodes
		n0.addToken(t0);                                                          
		n1.addToken(t1);                                                          
		n2.addToken(t2);                                                          
		n3.addToken(t3);                                                          
		n4.addToken(t4);                                                          
		n5.addToken(t5);                                                          
				 
		//make and add edges
		Edge e0 = new Edge(n1);                                                   
		n0.addEdge(e0);                                                           
		Edge e1 = new Edge(n2);                                                   
		n1.addEdge(e1);                                                           
		Edge e2 = new Edge(n3);                                                   
		n2.addEdge(e2);                                                           
		Edge e3 = new Edge(n4);                                                   
		n3.addEdge(e3);                                                           
		Edge e4= new Edge(n5);                                                    
		n4.addEdge(e4);                                                           
                                                                          
		ArrayList<Node> nodes = new ArrayList<>(Arrays.asList(n0,n1,n2,n3,n4,n5));
		Graph testGraph = new Graph(nodes);                                           
		Properties pTest = new Properties(testGraph);                                     
		//assertions
		assertTrue(pTest.getPlanar());                                   
	}

	@Test
	void notPlanar(){
		//make tokens
		Token t0 = new Token(0);                                                  
		Token t1 = new Token(1);                                                  
		Token t2 = new Token(2);                                                  
		Token t3 = new Token(3);                                                  
		Token t4 = new Token(4);                                                  
		Token t5 = new Token(5);                                                  
			
		//make nodes
		Node n0 = new Node(0);                                                    
		Node n1 = new Node(1);                                                    
		Node n2 = new Node(2);                                                    
		Node n3 = new Node(3);                                                    
		Node n4 = new Node(4);                                                    
		Node n5 = new Node(5);                                                    
		  
		//add tokens to nodes
		n0.addToken(t0);                                                          
		n1.addToken(t1);                                                          
		n2.addToken(t2);                                                          
		n3.addToken(t3);                                                          
		n4.addToken(t4);                                                          
		n5.addToken(t5);                                                          
				 
		//make and add edges
		Edge e0 = new Edge(n1);                                                   
		n0.addEdge(e0);                                                           
		Edge e1 = new Edge(n2);                                                   
		n1.addEdge(e1);                                                           
		Edge e2 = new Edge(n3);                                                   
		n2.addEdge(e2);                                                           
		Edge e3 = new Edge(n4);                                                   
		n3.addEdge(e3);                                                           
		Edge e4= new Edge(n5);                                                    
		n4.addEdge(e4); 
		//crossing edges  
		Edge e31 = new Edge(n1);
		n3.addEdge(e31);        
		Edge e20 = new Edge(n0);
		n2.addEdge(e20);                                                         
                                                                          
		ArrayList<Node> nodes = new ArrayList<>(Arrays.asList(n0,n1,n2,n3,n4,n5));
		Graph testGraph = new Graph(nodes);                                           
		Properties pTest = new Properties(testGraph);                                     
		//assertions
		assertFalse(pTest.getPlanar());       
	}

	@Test
	void connectedPlanarPath(){
		//make tokens
		Token t0 = new Token(0);                                                  
		Token t1 = new Token(1);                                                  
		Token t2 = new Token(2);                                                  
		Token t3 = new Token(3);                                                  
		Token t4 = new Token(4);                                                  
		Token t5 = new Token(5);                                                  
			
		//make nodes
		Node n0 = new Node(0);                                                    
		Node n1 = new Node(1);                                                    
		Node n2 = new Node(2);                                                    
		Node n3 = new Node(3);                                                    
		Node n4 = new Node(4);                                                    
		Node n5 = new Node(5);                                                    
		  
		//add tokens to nodes, with more than one token per node for some nodes
		n0.addToken(t0);
		n0.addToken(t1);                                                          
		n1.addToken(t1);                                                          
		n2.addToken(t2);   
		n2.addToken(t3);                                                       
		n3.addToken(t3);                                                          
		n4.addToken(t4);                                                          
		n5.addToken(t5);                                                          
				 
		//make and add edges
		Edge e0 = new Edge(n1);                                                   
		n0.addEdge(e0);                                                           
		Edge e1 = new Edge(n2);                                                   
		n1.addEdge(e1);                                                           
		Edge e2 = new Edge(n3);                                                   
		n2.addEdge(e2);                                                           
		Edge e3 = new Edge(n4);                                                   
		n3.addEdge(e3);                                                           
		Edge e4= new Edge(n5);                                                    
		n4.addEdge(e4);   
		
		//add adjacent nodes
		n1.addAdjacent(n0);
		n2.addAdjacent(n1);
		n3.addAdjacent(n2);
		n4.addAdjacent(n3);
		n5.addAdjacent(n4);
                                                                          
		ArrayList<Node> nodes = new ArrayList<>(Arrays.asList(n0,n1,n2,n3,n4,n5));
		Graph testGraph = new Graph(nodes);                                           
		Properties pTest = new Properties(testGraph);                                     
		//assertions
		assertTrue(pTest.getPlanar());
		assertTrue(pTest.getConnected());
		assertEquals(5, pTest.getLongestPath());
		assertTrue(n0.getLableMatch());
		assertTrue(n1.getLableMatch());
		assertTrue(n2.getLableMatch());
		assertTrue(n3.getLableMatch());
		assertTrue(n4.getLableMatch());
		assertTrue(n5.getLableMatch());
	}

	//first node visited and thus root node in the dfs tree, is a cut vertex
	//tests the first condition
	@Test
	void cutVertexRoot(){
		Node n0 = new Node(0);                                                             
		Node n1 = new Node(1);                                                             
		Node n2 = new Node(2);                                                             
		Node n3 = new Node(3);                                                                                                                                            
		Node n4 = new Node(4);                                                                                                                                                                                                                                                                                        
                                                                                   
		Edge e01 = new Edge(n1);                                                           
		Edge e12 = new Edge(n2);                                                           
		Edge e23 = new Edge(n3);                                                           
		Edge e03 = new Edge(n3);                                                                                                                                        
		Edge e04 = new Edge(n4);                                                                                                                                       
                                                                                   
		n0.addEdge(e01);                                                                   
		n1.addAdjacent(n0);                                                                
		n1.addEdge(e12);                                                                   
		n2.addAdjacent(n1);                                                                
		n2.addEdge(e23);                                                                   
		n3.addAdjacent(n2);                                                                                                                                      
		n0.addEdge(e04);                                                                 
		n4.addAdjacent(n0);                                                              	                                                                               
		n0.addEdge(e03);                                                                   
		n3.addAdjacent(n0);                                                                                                                                             
                                                                        
		ArrayList<Node> nodes = new ArrayList<>(Arrays.asList(n0,n1,n2,n3,n4));            
		Graph testGraph = new Graph(nodes);                                                        
		Properties pTest = new Properties(testGraph);  
		assertTrue(n0.getSpanMatch());
		assertFalse(n1.getSpanMatch());
		assertFalse(n2.getSpanMatch());
		assertFalse(n3.getSpanMatch());
		assertFalse(n4.getSpanMatch());                                              
	}

	@Test
	void cutVertexNotRoot(){
		Node n0 = new Node(0);                                                             
		Node n1 = new Node(1);                                                             
		Node n2 = new Node(2);                                                             
		Node n3 = new Node(3);                                                                                                                                                                                                                   
		Node n4 = new Node(4);                                                                                                                                                                                                                    
                                                                                   
		Edge e01 = new Edge(n1);                                                           
		Edge e12 = new Edge(n2);                                                           
		Edge e23 = new Edge(n3);                                                           
		Edge e03 = new Edge(n3);                                                                                                                                                                                                                   
		Edge e14 = new Edge(n4);                                                                                                                                                                                                                  
                                                                                   
		n0.addEdge(e01);                                                                   
		n1.addAdjacent(n0);                                                                
		n1.addEdge(e12);                                                                   
		n2.addAdjacent(n1);                                                                
		n2.addEdge(e23);                                                                   
		n3.addAdjacent(n2);                                                                                                                                               
		n0.addEdge(e03);                                                                   
		n3.addAdjacent(n0);                                                                                                                                        
		n1.addEdge(e14);                                                                   
		n4.addAdjacent(n1);                                                                                                                                               
                                                                                                                                                         
		ArrayList<Node> nodes = new ArrayList<>(Arrays.asList(n0,n1,n2,n3,n4));             
		Graph testGraph = new Graph(nodes);                                                        
		Properties pTest = new Properties(testGraph);
		assertTrue(n1.getSpanMatch());
		assertFalse(n0.getSpanMatch());
		assertFalse(n2.getSpanMatch());
		assertFalse(n3.getSpanMatch());
		assertFalse(n4.getSpanMatch());
	}

	@Test
	void noCutVertex(){
		Node n0 = new Node(0);                                                             
		Node n1 = new Node(1);                                                             
		Node n2 = new Node(2);                                                             
		
		//make edges
		Edge e01 = new Edge(n1);
		Edge e12 = new Edge(n2);
		Edge e02 = new Edge(n2);

		//add edges and adjacent
		n0.addEdge(e01);
		n1.addAdjacent(n0);
		n1.addEdge(e12);
		n2.addAdjacent(n1);
		n0.addEdge(e02);
		n2.addAdjacent(n0);

		ArrayList<Node> nodes = new ArrayList<>(Arrays.asList(n0,n1,n2));             
		Graph testGraph = new Graph(nodes);                                                        
		Properties pTest = new Properties(testGraph);
		assertFalse(n1.getSpanMatch());
		assertFalse(n0.getSpanMatch());
		assertFalse(n2.getSpanMatch());
	}

	@Test
	void cutVerticesNotConnected(){
		Node n0 = new Node(0);                                                             
		Node n1 = new Node(1);                                                             
		Node n2 = new Node(2);                                                             
		Node n3 = new Node(3);                                                                                                                                                                                                                   
		Node n4 = new Node(4); 
		Node n5 = new Node(5);
		Node n6 = new Node(6);
		Node n7 = new Node(7);                                                                                                                                                                                                                   
                                                                                   
		Edge e01 = new Edge(n1);                                                           
		Edge e12 = new Edge(n2);                                                           
		Edge e23 = new Edge(n3);                                                           
		Edge e03 = new Edge(n3);                                                                                                                                                                                                                   
		Edge e14 = new Edge(n4); 
		Edge e56 = new Edge(n6); 
		Edge e67 = new Edge(n7);  
		                                                                                                                                                                                                                
                                                                                   
		n0.addEdge(e01);                                                                   
		n1.addAdjacent(n0);                                                                
		n1.addEdge(e12);                                                                   
		n2.addAdjacent(n1);                                                                
		n2.addEdge(e23);                                                                   
		n3.addAdjacent(n2);                                                                                                                                               
		n0.addEdge(e03);                                                                   
		n3.addAdjacent(n0);                                                                                                                                        
		n1.addEdge(e14);                                                                   
		n4.addAdjacent(n1);  
		n5.addEdge(e56);    
		n6.addAdjacent(n5); 
		n6.addEdge(e67);    
		n7.addAdjacent(n6);                                                                                                                                              
                                                                                                                                                         
		ArrayList<Node> nodes = new ArrayList<>(Arrays.asList(n0,n1,n2,n3,n4,n5,n6,n7));             
		Graph testGraph = new Graph(nodes);                                                        
		Properties pTest = new Properties(testGraph);
		assertTrue(n1.getSpanMatch());
		assertFalse(n0.getSpanMatch());
		assertFalse(n2.getSpanMatch());
		assertFalse(n3.getSpanMatch());
		assertFalse(n4.getSpanMatch());
		assertTrue(n6.getSpanMatch());
		assertFalse(n7.getSpanMatch());
		assertFalse(n5.getSpanMatch());
	}

	//check longest path and cut vertex flags aren't overriding each other
	@Test
	void cutVertexLongPath(){
		Node n0 = new Node(0);                                                             
		Node n1 = new Node(1);                                                             
		Node n2 = new Node(2);                                                             
		
		//make edges
		Edge e01 = new Edge(n1);
		Edge e12 = new Edge(n2);

		//add edges and adjacent
		n0.addEdge(e01);
		n1.addAdjacent(n0);
		n1.addEdge(e12);
		n2.addAdjacent(n1);

		ArrayList<Node> nodes = new ArrayList<>(Arrays.asList(n0,n1,n2));             
		Graph testGraph = new Graph(nodes);                                                        
		Properties pTest = new Properties(testGraph);
		assertTrue(n1.getSpanMatch());
		assertFalse(n0.getSpanMatch());
		assertFalse(n2.getSpanMatch());
		assertTrue(n1.getLableMatch());
		assertTrue(n0.getLableMatch());
		assertTrue(n2.getLableMatch());
		assertEquals(2, pTest.getLongestPath());
	}

	@Test
	//graph should be planar, connected and have a longest path of 2
	void allPropertiesUsingFile(){
		Properties pTest = new Properties(lib.getGraph(0));
		//assertions
		assertTrue(pTest.getConnected());
		assertTrue(pTest.getPlanar());
		assertEquals(2, pTest.getLongestPath());
	}

	@Test
	void notPlanarFile(){
		Properties pTest = new Properties(lib.getGraph(1));
		//assertions
		assertTrue(pTest.getConnected());
		assertFalse(pTest.getPlanar());
		assertEquals(2, pTest.getLongestPath());
	}

}

class RepgraphGraphPatternsTests{

	DMRSLibrary lib = new DMRSLibrary("src/test/resources/wsj00a.dmrs");

	@Test
	void simplePatternMatch(){
		//Pattern                   
		Node n1 = new Node("1");    
		Node n2 = new Node("2");    
		Node n3 = new Node("3");    
		Node n4 = new Node("4");    
                            
		Edge A = new Edge("A", n2); 
		Edge B = new Edge("B", n3); 
		Edge C = new Edge("C", n4); 
                            
		n1.addEdge(A);              
		n1.addEdge(B);              
		n3.addEdge(C); 
		
		//Graph to check for pattern                                                                                                                           
     	Node n15 = new Node("5");                                             
     	Node n16 = new Node("6");                                             
     	Node n17 = new Node("7");                                                                                                                         
		Node n11 = new Node("1");                                                       
		Node n12 = new Node("2");                                                       
		Node n13 = new Node("3");                                                       
		Node n14 = new Node("4");                                                                                                             
                                                                                                                                                      
		Edge D = new Edge("A",n16);                                                     
		Edge E = new Edge("B", n17);                                                                                                                        
		Edge F = new Edge("F", n11);                                                    
		Edge A1 = new Edge("A", n12);                                                   
		Edge B1 = new Edge("B", n13);                                                                                                                 
		Edge C1 = new Edge("C", n14);                                                                                                     
                                                                                
		n15.addEdge(D);                                                                 
		n15.addEdge(E);                                                                 
		n17.addEdge(F);                                                                 
		n11.addEdge(A1);                                                                
		n11.addEdge(B1);                                                                
		n13.addEdge(C1);                                                                                                                               
                                                                                
		ArrayList<Node> n = new ArrayList<>(Arrays.asList(n15,n16,n17,n11,n12,n13,n14));
		Graph testGraph = new Graph(n);                                                         
		PatternMatch pattern = new PatternMatch(n1); 
		boolean match = pattern.graphMatch(testGraph);
		//assertions
		assertTrue(match);
		assertTrue(n11.getLableMatch());
		assertTrue(n12.getLableMatch());
		assertTrue(n13.getLableMatch());
		assertTrue(n14.getLableMatch());
		assertTrue(A1.getEdgeMatch());
		assertTrue(B1.getEdgeMatch());
		assertTrue(C1.getEdgeMatch());
		//false assertions
		assertFalse(n15.getLableMatch());
		assertFalse(n16.getLableMatch());
		assertFalse(n17.getLableMatch());
		assertFalse(D.getEdgeMatch());
		assertFalse(E.getEdgeMatch());
		assertFalse(F.getEdgeMatch());
	}

	//Pattern being searched for is a subgraph within the graph
	@Test
	void PatternWithExtraNodes(){
		//Pattern                   
		Node n1 = new Node("1");    
		Node n2 = new Node("2");    
		Node n3 = new Node("3");    
		Node n4 = new Node("4");    
                            
		Edge A = new Edge("A", n2); 
		Edge B = new Edge("B", n3); 
		Edge C = new Edge("C", n4); 
                            
		n1.addEdge(A);              
		n1.addEdge(B);              
		n3.addEdge(C); 
		
		//Graph to check for pattern                                                                                                                                                                                                       
		Node n11 = new Node("1");                                                       
		Node n12 = new Node("2");                                                       
		Node n13 = new Node("3");                                                       
		Node n14 = new Node("4");                                                                            
		Node n18 = new Node("8");                                                       
                                                                                                                                                                                                         
		Edge A1 = new Edge("A", n12);                                                   
		Edge B1 = new Edge("B", n13);                                                                                                                 
		Edge C1 = new Edge("C", n14);                                                                                                                           
		Edge G = new Edge("G", n18);                                                    
                                                                                                                                                
		n11.addEdge(A1);                                                                
		n11.addEdge(B1);                                                                
		n13.addEdge(C1);                                                                                                                                  
		n14.addEdge(G);                                                                 
                                                                                
		ArrayList<Node> n = new ArrayList<>(Arrays.asList(n11,n12,n13,n14));
		Graph testGraph = new Graph(n);                                                         
		PatternMatch pattern = new PatternMatch(n1);
		boolean matched = pattern.graphMatch(testGraph);
		//assertions
		assertTrue(matched);
		assertTrue(n11.getLableMatch());
		assertTrue(n12.getLableMatch());
		assertTrue(n13.getLableMatch());
		assertTrue(n14.getLableMatch());
		assertTrue(A1.getEdgeMatch());
		assertTrue(B1.getEdgeMatch());
		assertTrue(C1.getEdgeMatch());
		//false assertions
		assertFalse(n18.getLableMatch());
		assertFalse(G.getEdgeMatch());

	}

	@Test
	void multiplePatterns(){
		//Pattern                   
		Node n1 = new Node("1");    
		Node n2 = new Node("2");    
		Node n3 = new Node("3");       
                            
		Edge A = new Edge("A", n2); 
		Edge B = new Edge("B", n3);  
                            
		n1.addEdge(A);              
		n1.addEdge(B);              
		
		//Graph to check for pattern                                                                                                                              
		Node n15 = new Node("1");                                                       
		Node n16 = new Node("2");                                                       
		Node n17 = new Node("3");                                                                                                                              
		Node n11 = new Node("1");                                                       
		Node n12 = new Node("2");                                                       
		Node n13 = new Node("3");                                                       
		Node n14 = new Node("4");                                                                                                           
                                                                                                                                                   
		Edge D = new Edge("A",n16);                                                     
		Edge E = new Edge("B", n17);                                                                                                                       
		Edge F = new Edge("F", n11);                                                    
		Edge A1 = new Edge("A", n12);                                                   
		Edge B1 = new Edge("B", n13);                                                   
		//Pattern match                                                                 
		Edge C1 = new Edge("C", n14);                                                                                                                                          
                                                                                
		n15.addEdge(D);                                                                 
		n15.addEdge(E);                                                                 
		n17.addEdge(F);                                                                 
		n11.addEdge(A1);                                                                
		n11.addEdge(B1);                                                                
		n13.addEdge(C1);                                                                                                                               
                                                                                
		ArrayList<Node> n = new ArrayList<>(Arrays.asList(n15,n16,n17,n11,n12,n13,n14));
		Graph testGraph = new Graph(n);                                                         
		PatternMatch pattern = new PatternMatch(n1);
		boolean match = pattern.graphMatch(testGraph);
		//assertions
		assertTrue(match);
		assertTrue(n11.getLableMatch());
		assertTrue(n12.getLableMatch());
		assertTrue(n13.getLableMatch());
		assertTrue(A1.getEdgeMatch());
		assertTrue(B1.getEdgeMatch());
		assertTrue(n15.getLableMatch());
		assertTrue(n16.getLableMatch());
		assertTrue(n17.getLableMatch());
		assertTrue(D.getEdgeMatch());
		assertTrue(E.getEdgeMatch());
		//false assertions
		assertFalse(F.getEdgeMatch());
		assertFalse(n14.getLableMatch());
		assertFalse(C1.getEdgeMatch());
	}

	@Test
	void noPatternMatch(){
		//Pattern                   
		Node n1 = new Node("6");    
		Node n2 = new Node("7");    
		Node n3 = new Node("8");       
                            
		Edge A = new Edge("A", n2); 
		Edge B = new Edge("B", n3); 
                            
		n1.addEdge(A);              
		n1.addEdge(B);               
		
		//Graph to check for pattern                                                                                                                            
		Node n11 = new Node("1");                                                       
		Node n12 = new Node("2");                                                       
		Node n13 = new Node("3");                                                       
		Node n14 = new Node("4");                                                                                                             
                                                                                                                                                                                                         
		Edge A1 = new Edge("A", n12);                                                   
		Edge B1 = new Edge("B", n13);                                                   
		//Pattern match                                                                 
		Edge C1 = new Edge("C", n14);                                                                                                      
                                                                                                                                             
		n11.addEdge(A1);                                                                
		n11.addEdge(B1);                                                                
		n13.addEdge(C1);                                                                                                                                
                                                                                
		ArrayList<Node> n = new ArrayList<>(Arrays.asList(n11,n12,n13,n14));
		Graph testGraph = new Graph(n);                                                         
		PatternMatch pattern = new PatternMatch(n1);
		boolean match = pattern.graphMatch(testGraph);
		//assertions
		assertFalse(match);
	}

	@Test
	void noPatternEdgeMismatch(){
		//Pattern                   
		Node n1 = new Node("6");    
		Node n2 = new Node("7");    
		Node n3 = new Node("8");       
                            
		Edge A = new Edge("A", n2); 
		Edge B = new Edge("B", n3); 
                            
		n1.addEdge(A);              
		n1.addEdge(B);               
		
		//Graph to check for pattern                                                                                                                            
		Node n11 = new Node("6");                                                       
		Node n12 = new Node("7");                                                       
		Node n13 = new Node("8");                                                                                                                                                                    
                                                                                                                                                                                                         
		Edge A1 = new Edge("A", n12);                                                   
		Edge B1 = new Edge("F", n13);                                                                                                                                                         
                                                                                                                                             
		n11.addEdge(A1);                                                                
		n11.addEdge(B1);                                                                                                                                                                                               
                                                                                
		ArrayList<Node> n = new ArrayList<>(Arrays.asList(n11,n12,n13));
		Graph testGraph = new Graph(n);                                                         
		PatternMatch pattern = new PatternMatch(n1);
		boolean match = pattern.graphMatch(testGraph);
		//assertions
		assertFalse(match);
	}

	@Test
	//graphs with labels 20001002 and 20037015 should match. for 20037015 n15 = 12, n17=14, n16=13 for wsj00a.dmrs
	void patternMatchWithFile(){
		Node n15 = new Node("udef_q");
         Node n16 = new Node("_publish_v_1");
         Node n17 = new Node("nominalization");
         Edge e = new Edge("RSTR", n17);
         Edge e1 = new Edge("ARG1", n16);
         n15.addEdge(e);
		 n17.addEdge(e1);
		 
		 ArrayList<Graph> matches = lib.findPattern(n15);
		 //assertions
		 assertEquals(2, matches.size());
		 assertEquals("20001002", matches.get(0).getId());
		 assertEquals("20037015", matches.get(1).getId());
	}

}

class RepgraphGraphLabelMatchTests{

	DMRSLibrary lib = new DMRSLibrary("src/test/resources/testCompare.dmrs");

	//declaring two graphs for testing
	Node n1 = new Node("cat");
	Node n2 = new Node("dog");
	Node n3 = new Node("cat");
	//other graph
	Node n4 = new Node("dog");
	Node n5 = new Node("fish");
	
	//make dmrsLibrary variable for testing
	ArrayList<Node> nodes1 = new ArrayList<>(Arrays.asList(n1,n2,n3));
	ArrayList<Node> nodes2 = new ArrayList<>(Arrays.asList(n4,n5));
	Graph g1 = new Graph(nodes1);
	Graph g2 = new Graph(nodes2);
	DMRSLibrary libTest = new DMRSLibrary(new ArrayList<Graph>(Arrays.asList(g1,g2)));

	@Test
	void matchBothGraphs(){
		ArrayList<Graph> matched = libTest.findPatternSimple(new ArrayList<String>(Arrays.asList("dog")));
		//assertions
		assertEquals(2, matched.size());
		assertTrue(n2.getLableMatch());
		assertTrue(n4.getLableMatch());
		//false assertions
		assertFalse(n1.getLableMatch());
		assertFalse(n3.getLableMatch());
		assertFalse(n5.getLableMatch());
	}

	@Test
	void matchFirstGraphOnly(){
		ArrayList<Graph> matched = libTest.findPatternSimple(new ArrayList<String>(Arrays.asList("cat")));
		//assertions
		assertEquals(1, matched.size());
		assertFalse(n2.getLableMatch());
		assertFalse(n4.getLableMatch());
		assertTrue(n1.getLableMatch());
		assertTrue(n3.getLableMatch());
		assertFalse(n5.getLableMatch());
	}

	@Test
	void matchSecondGraphOnly(){
		ArrayList<Graph> matched = libTest.findPatternSimple(new ArrayList<String>(Arrays.asList("fish")));
		//assertions
		assertEquals(1, matched.size());
		assertFalse(n2.getLableMatch());
		assertFalse(n4.getLableMatch());
		assertFalse(n1.getLableMatch());
		assertFalse(n3.getLableMatch());
		assertTrue(n5.getLableMatch());
	}

	@Test
	void matchNoGraphs(){
		ArrayList<Graph> matched = libTest.findPatternSimple(new ArrayList<String>(Arrays.asList("cat","dog","fish","food")));
		//assertions
		assertEquals(0, matched.size());
		//variables will still be set to true, as there was matching, but not all labels were matched thus graph does not match
		assertTrue(n2.getLableMatch());
		assertTrue(n4.getLableMatch());
		assertTrue(n1.getLableMatch());
		assertTrue(n3.getLableMatch());
		assertTrue(n5.getLableMatch());
	}

	@Test
	void noMatchesWithFile(){
 		ArrayList<String> labelsNo = new ArrayList<>(Arrays.asList("cat", "udef_q"));
 		ArrayList<Graph> graphs = lib.findPatternSimple(labelsNo);
		//assertions
		assertEquals(graphs.size(), 0);
 		
	}

	@Test
	void matchesWithFile(){
		ArrayList<String> labelsYes = new ArrayList<>(Arrays.asList("proper_q", "compound"));
		ArrayList<Graph> graphs = lib.findPatternSimple(labelsYes);
 		//size should be 5 as matches in all graphs
 		assertEquals(graphs.size(), 5);
	}

	@Test
	void someMatchesFile(){
		ArrayList<String> labels2 = new ArrayList<>(Arrays.asList("nominalization"));
		ArrayList<Graph> graphs = lib.findPatternSimple(labels2);
 		//size should be 2
 		assertEquals(graphs.size(), 2);
	}

}

class RepGraphSubgraphTests{

	DMRSLibrary lib = new DMRSLibrary("src/test/resources/testCompare.dmrs");
	
	@Test
	void subgraphRootNode(){
		Node n0 = new Node("0");
		Node n1 = new Node("1");
		Node n2 = new Node("2");
		Node n3 = new Node("3");

		Edge e01 = new Edge(n1);
		Edge e02 = new Edge(n2);
		Edge e13 = new Edge(n3);
		Edge e23 = new Edge(n3);
	
		n0.addEdge(e01);
		n0.addEdge(e02);
		n1.addEdge(e13);
		n2.addEdge(e23);

		ArrayList<Node> nodes = new ArrayList<>(Arrays.asList(n0,n1,n2,n3));
		Graph g = new Graph(nodes);
		g.markSubgraph(n0);
		//assertions
		assertTrue(n0.getLableMatch());
		assertTrue(n1.getLableMatch());
		assertTrue(n2.getLableMatch());
		assertTrue(n3.getLableMatch());
		assertTrue(e01.getEdgeMatch());
		assertTrue(e02.getEdgeMatch());
		assertTrue(e13.getEdgeMatch());
		assertTrue(e23.getEdgeMatch());
	}

	@Test
	void subGraphNonRootNode(){
		Node n0 = new Node("0");
		Node n1 = new Node("1");
		Node n2 = new Node("2");
		Node n3 = new Node("3");

		Edge e01 = new Edge(n1);
		Edge e02 = new Edge(n2);
		Edge e13 = new Edge(n3);
		Edge e23 = new Edge(n3);
	
		n0.addEdge(e01);
		n0.addEdge(e02);
		n1.addEdge(e13);
		n2.addEdge(e23);

		ArrayList<Node> nodes = new ArrayList<>(Arrays.asList(n0,n1,n2,n3));
		Graph g = new Graph(nodes);
		g.markSubgraph(n1);
		//assertions
		assertFalse(n0.getLableMatch());
		assertTrue(n1.getLableMatch());
		assertFalse(n2.getLableMatch());
		assertTrue(n3.getLableMatch());
		assertFalse(e01.getEdgeMatch());
		assertFalse(e02.getEdgeMatch());
		assertTrue(e13.getEdgeMatch());
		assertFalse(e23.getEdgeMatch());
	}

	@Test
	void subgraphLeafNode(){
		Node n0 = new Node("0");
		Node n1 = new Node("1");
		Node n2 = new Node("2");
		Node n3 = new Node("3");

		Edge e01 = new Edge(n1);
		Edge e02 = new Edge(n2);
		Edge e13 = new Edge(n3);
		Edge e23 = new Edge(n3);
	
		n0.addEdge(e01);
		n0.addEdge(e02);
		n1.addEdge(e13);
		n2.addEdge(e23);

		ArrayList<Node> nodes = new ArrayList<>(Arrays.asList(n0,n1,n2,n3));
		Graph g = new Graph(nodes);
		g.markSubgraph(n3);
		//assertions
		assertFalse(n0.getLableMatch());
		assertFalse(n1.getLableMatch());
		assertFalse(n2.getLableMatch());
		assertTrue(n3.getLableMatch());
		assertFalse(e01.getEdgeMatch());
		assertFalse(e02.getEdgeMatch());
		assertFalse(e13.getEdgeMatch());
		assertFalse(e23.getEdgeMatch());
	}

	@Test
	void subgraphClearsBeforeRun(){
		Node n0 = new Node("0");
		Node n1 = new Node("1");
		Node n2 = new Node("2");
		Node n3 = new Node("3");
		Node n4 = new Node("4");

		Edge e01 = new Edge(n1);
		Edge e02 = new Edge(n2);
		Edge e23 = new Edge(n3);
		Edge e24 = new Edge(n4);

	
		n0.addEdge(e01);
		n0.addEdge(e02);
		n2.addEdge(e23);
		n2.addEdge(e24);

		ArrayList<Node> nodes = new ArrayList<>(Arrays.asList(n0,n1,n2,n3,n4));
		Graph g = new Graph(nodes);
		g.markSubgraph(n0);
		//assertions
		assertTrue(n0.getLableMatch());
		assertTrue(n1.getLableMatch());
		assertTrue(n2.getLableMatch());
		assertTrue(n3.getLableMatch());
		assertTrue(n4.getLableMatch());
		assertTrue(e01.getEdgeMatch());
		assertTrue(e02.getEdgeMatch());
		assertTrue(e23.getEdgeMatch());
		assertTrue(e24.getEdgeMatch());
		//mark another graph
		g.markSubgraph(n2);
		//assertions
		assertFalse(n0.getLableMatch());
		assertFalse(n1.getLableMatch());
		assertTrue(n2.getLableMatch());
		assertTrue(n3.getLableMatch());
		assertTrue(n4.getLableMatch());
		assertFalse(e01.getEdgeMatch());
		assertFalse(e02.getEdgeMatch());
		assertTrue(e23.getEdgeMatch());
		assertTrue(e24.getEdgeMatch());
	}

	@Test
	void subgraphWithFile(){
		Graph g = lib.getGraph(0);
		g.markSubgraph(g.getNodes().get(20));
		ArrayList<Node> nodes  = g.getNodes();
		for (int i = 0; i < g.getNodeCount(); i++){
			Node n = nodes.get(i);
			if (i == 20 || i == 6 || i == 8 || i == 18){
				assertTrue(n.getLableMatch());
				for (Edge e: n.getEdges()){
					assertTrue(e.getEdgeMatch());
				}
			}
			else {
				assertFalse(n.getLableMatch());
				for (Edge e: n.getEdges()){
					assertFalse(e.getEdgeMatch());
				}
			}
		}
	}
}


class RepgraphCompareGraphsTests{
	DMRSLibrary lib = new DMRSLibrary("src/test/resources/testCompare.dmrs");
 	Graph g0 = lib.getGraph(0);
 	Graph g1 = lib.getGraph(1);
	Graph g2 = lib.getGraph(2);
	Graph g3 = lib.getGraph(3);
	Graph g4 = lib.getGraph(4);
	 
	@Test
	void compareSameGraph(){
		g0.compareGraph(g0);
		//all should match since being compared to itself
		for (Node n: g0.getNodes()){
			assertTrue(n.getLableMatch());
			assertTrue(n.getSpanMatch());
			for (Edge e: n.getEdges()){
				assertTrue(e.getEdgeMatch());
			}
		}
	}

	@Test
	void compareDifferentGraphs(){
		g0.compareGraph(g2);
		//check g0, should have no edge matches, only n13 has spanMatch
		for (int i = 0; i < g0.getNodeCount(); i++){
			Node n = g0.getNodes().get(i);
			if (i == 13){
				assertTrue(n.getSpanMatch());
			}
			else {
				assertFalse(n.getSpanMatch());
			}
			if ( i==0 || i ==2 || i == 3 || i ==4 || i == 8 || i == 10 || i == 11 || i == 12 || i ==13 || i ==15 || i==19 || i==21){
				assertTrue(n.getLableMatch());
			}
			else {
				assertFalse(n.getLableMatch());
			}
			for (Edge e: n.getEdges()){
				assertFalse(e.getEdgeMatch());
			}
		}
		//check g2, should have no edge matches, only n11 has spanMatch
		for (int i = 0; i < g2.getNodeCount(); i++){
			Node n = g2.getNodes().get(i);
			if (i == 11){
				assertTrue(n.getSpanMatch());
			}
			else {
				assertFalse(n.getSpanMatch());
			}
			if ( i==0 || i == 1 || i ==2 || i ==3 || i ==7 || i ==9 || i == 11){
				assertTrue(n.getLableMatch());
			}
			else {
				assertFalse(n.getLableMatch());
			}
			for (Edge e: n.getEdges()){
				assertFalse(e.getEdgeMatch());
			}
		}
	}

	@Test
	//Graphs are the same except one node has its token span changed
	void compareGraphWithOneSpanMismatch(){
		g1.compareGraph(g0);
		//check g0, all labels should be true except for spanMatch n13 and edge from 13 to 18
		for (int i = 0; i < g0.getNodeCount(); i++){
			Node n = g0.getNodes().get(i);
			if (i == 13){
				assertFalse(n.getSpanMatch());
			}
			else {
				assertTrue(n.getSpanMatch());
			}
			assertTrue(n.getLableMatch());
			if ( i == 13){
				for (Edge e: n.getEdges()){
					assertFalse(e.getEdgeMatch());
				}
			}
			else {
				for (Edge e: n.getEdges()){
					assertTrue(e.getEdgeMatch());
				}
			}
		}
		//check g0, all labels should be true except for spanMatch n13 and edge from 13 to 18
		for (int i = 0; i < g1.getNodeCount(); i++){
			Node n = g1.getNodes().get(i);
			if (i == 13){
				assertFalse(n.getSpanMatch());
			}
			else {
				assertTrue(n.getSpanMatch());
			}
			assertTrue(n.getLableMatch());
			if ( i == 13){
				for (Edge e: n.getEdges()){
					assertFalse(e.getEdgeMatch());
				}
			}
			else {
				for (Edge e: n.getEdges()){
					assertTrue(e.getEdgeMatch());
				}
			}
		}
	}

	@Test
	//graphs are the same except label on one of the nodes has been changed
	void compareGraphWithOneLabelMismatch(){
		g3.compareGraph(g2); 
		//should have label mismatch for n1 in g3, but no mismatches in g2 since changed
		//label had multiple copies
		for (int i = 0; i < g2.getNodeCount(); i++){
			Node n = g2.getNodes().get(i);
			assertTrue(n.getLableMatch());
			for (Edge e: n.getEdges()){
				assertTrue(e.getEdgeMatch());
			}
		}
		//for g3
		for (int i = 0; i < g3.getNodeCount(); i++){
			Node n = g3.getNodes().get(i);
			if (i == 1){
				assertFalse(n.getLableMatch());
			}
			else {
				assertTrue(n.getLableMatch());
			}
			assertTrue(n.getSpanMatch());
			for (Edge e: n.getEdges()){
					assertTrue(e.getEdgeMatch());
			}
		}
	}

	@Test
	//Graphs are the same except the label on one of the edges have been changed
	void compareGraphOneEdgeLabelMismatch(){
		//edge in g4 for 9->2 has changed graph label NotRSTR. Means edge in other graph will
		//also have a mismatch
		g4.compareGraph(g2);
		for (int i = 0; i < g2.getNodeCount(); i++){
			Node n = g2.getNodes().get(i);
			assertTrue(n.getLableMatch());
			for (Edge e: n.getEdges()){
				if ( i == 9 && e.getDest().getID() == 2){
					assertFalse(e.getEdgeMatch());
				}
				else {
					assertTrue(e.getEdgeMatch());
				}
			}
		}
		//for g4
		for (int i = 0; i < g4.getNodeCount(); i++){
			Node n = g4.getNodes().get(i);
			assertTrue(n.getLableMatch());
			for (Edge e: n.getEdges()){
				if ( i == 9 && e.getDest().getID() == 2){
					assertFalse(e.getEdgeMatch());
				}
				else {
					assertTrue(e.getEdgeMatch());
				}
			}
		}

	}
}