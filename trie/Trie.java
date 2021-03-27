package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Shiv Joshi
 * java -cp bin trie.TrieApp
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {

		//create empty root and point to first child
		Indexes substr = new Indexes(0, (short)0, (short)(allWords[0].length()-1));
		TrieNode first = new TrieNode(substr, null, null);
		TrieNode root = new TrieNode(null, first, null);

		TrieNode curr = root.firstChild;

		//prefixes
		curr = root.firstChild;
		short end = 0;
		TrieNode pointerP = curr;
		int p = 1;
		
		
		for(int i = 1; i<allWords.length; i++){
			end=0;
			
			pointerP = curr;
			String t1 = allWords[i].substring(0, end+1);
			String t2 = allWords[curr.substr.wordIndex].substring(0, end+1);

			if(!t1.equals(t2)){
				//enter all code if word doesn't match
				//make a new method

				//finding next prefix
				p=1;
				while(!t1.equals(t2) && pointerP.sibling!=null){
					pointerP=pointerP.sibling;
					t1 = allWords[i].substring(0, end+1);
					t2 = allWords[pointerP.substr.wordIndex].substring(0, end+1);
					p++;
				}
				
				//if no prefix found create sibling
				if(!t1.equals(t2)){
					TrieNode pointerS = curr;
						
					Indexes tempI = new Indexes(i, (short)0, (short)(allWords[i].length()-1));
					TrieNode tempN = new TrieNode(tempI, null, null);
						
					while(pointerS.sibling!=null){
						pointerS=pointerS.sibling;
					}
					pointerS.sibling = tempN;
				}

				if(pointerP.firstChild==null && t1.equals(t2)){
					Indexes prefixI = new Indexes(pointerP.substr.wordIndex, (short)0, end);

					while(t1.equals(t2)){
						Indexes newE = new Indexes(i, (short)(end+1), (short)(allWords[i].length()-1));
						TrieNode newN = new TrieNode(newE, null, null);

						Indexes oldE = new Indexes(pointerP.substr.wordIndex, (short)(end+1), (short)(allWords[pointerP.substr.wordIndex].length()-1));
						TrieNode oldN = new TrieNode(oldE, null, newN);

						TrieNode prefix = new TrieNode(prefixI, pointerP.firstChild, pointerP.sibling);
						pointerP = prefix;
					
						pointerP.firstChild = oldN;

						end++;
						t1 = allWords[i].substring(0, end+1);
						t2 = allWords[pointerP.substr.wordIndex].substring(0, end+1);
						prefixI = new Indexes(pointerP.substr.wordIndex, (short)0, end);
					}
				}else if(pointerP.firstChild!=null && t1.equals(t2)){
					t1 = allWords[i].substring(pointerP.substr.startIndex, pointerP.substr.endIndex+1);
					t2 = allWords[pointerP.substr.wordIndex].substring(pointerP.substr.startIndex, pointerP.substr.endIndex+1);

					
					//if the prefix is the same for the new word(compare w curr.firstchild)
					if(t1.equals(t2)){
						Indexes tI = new Indexes(i, (short)(pointerP.substr.endIndex+1), (short)(allWords[i].length()-1));
						TrieNode tN = new TrieNode(tI, null, null);
						TrieNode header = pointerP.firstChild;
						

						pointerP.firstChild = addToEnd(header, tN);

						//compare additional prefix w firstChild(compare w siblings of curr.firstchild)
						header = pointerP.firstChild;

						t1="a";
						t2="b";
						int c = 0;
						while(!t1.equals(t2) && header!=tN){
							int diff = header.substr.endIndex-header.substr.startIndex;
							if(diff==0){
								t1 = allWords[i].substring(header.substr.startIndex, header.substr.endIndex+1);
								t2 = allWords[header.substr.wordIndex].substring(header.substr.startIndex, header.substr.endIndex+1);
							}else{
								if(header.substr.endIndex<allWords[i].length()){
									t1 = allWords[i].substring(header.substr.startIndex, header.substr.endIndex);
									t2 = allWords[header.substr.wordIndex].substring(header.substr.startIndex, header.substr.endIndex);
								}
							}

							if(t1.equals(t2)){
								//create new node for the new word which is the sibling
								Indexes nI = new Indexes(i, (short)(header.substr.endIndex), (short)(allWords[i].length()-1));
								TrieNode nN = new TrieNode(nI, null, null);
	
								//create new node for the new firstchild
								Indexes oI = new Indexes(header.substr.wordIndex, (short)(header.substr.endIndex), header.substr.endIndex);
								TrieNode oN = new TrieNode(oI, header.firstChild, nN);
	
								//create the new curr/root.firstchild
								header = removeFromEnd(header);
								Indexes tempI = new Indexes(header.substr.wordIndex, header.substr.startIndex, (short)(header.substr.endIndex-1));
								TrieNode tempN = new TrieNode(tempI, oN, header.sibling);
	
								header = tempN;
								
								pointerP.firstChild = find(pointerP.firstChild, c, header);
							}


							if(!t1.equals(t2)){
								int shorten = -1;
								diff = (header.substr.endIndex+shorten)-header.substr.startIndex;
				
								while(!t1.equals(t2) && diff>0){
									if((header.substr.endIndex+shorten)>header.substr.startIndex){
										t1 = allWords[i].substring(header.substr.startIndex, header.substr.endIndex+shorten);
										t2 = allWords[header.substr.wordIndex].substring(header.substr.startIndex, header.substr.endIndex+shorten);
										shorten--;
										diff = (header.substr.endIndex+shorten)-header.substr.startIndex;
									}else{break;}
								}

								
								if(t1.equals(t2)){
									//create new node for the new word which is the sibling
									Indexes nI = new Indexes(i, (short)(header.substr.endIndex+shorten+1), (short)(allWords[i].length()-1));
									TrieNode nN = new TrieNode(nI, null, null);
		
									//create new node for the new firstchild
									Indexes oI = new Indexes(header.substr.wordIndex, (short)(header.substr.endIndex+shorten+1), header.substr.endIndex);
									TrieNode oN = new TrieNode(oI, header.firstChild, nN);
		
									//create the new curr/root.firstchild
									header = removeFromEnd(header);

									Indexes tempI = new Indexes(header.substr.wordIndex, header.substr.startIndex, (short)(header.substr.endIndex+shorten));
									TrieNode tempN = new TrieNode(tempI, oN, header.sibling);
									
									if(header==pointerP.firstChild){
										header = tempN;
										pointerP.firstChild=header;
									}else{
										header = tempN;
										pointerP.firstChild = removeFromEnd(pointerP.firstChild);
										pointerP.firstChild = addToEnd(pointerP.firstChild, header);
									}
										
								}

							}
							header = header.sibling;
							c++;
						}
					}
				}
				curr = find(curr, p, pointerP);

			}else{
				
				if(curr.firstChild==null){
					t1 = allWords[i].substring(0, end+1);
					t2 = allWords[curr.substr.wordIndex].substring(0, end+1);
	
					if(t1.equals(t2)){
						Indexes prefixI = new Indexes(curr.substr.wordIndex, (short)0, end);
	
						while(t1.equals(t2)){
							Indexes newE = new Indexes(i, (short)(end+1), (short)(allWords[i].length()-1));
							TrieNode newN = new TrieNode(newE, null, null);
	
							Indexes oldE = new Indexes(curr.substr.wordIndex, (short)(end+1), (short)(allWords[curr.substr.wordIndex].length()-1));
							TrieNode oldN = new TrieNode(oldE, null, newN);
	
							TrieNode prefix = new TrieNode(prefixI, curr.firstChild, curr.sibling);
							curr = prefix;
							curr.firstChild = oldN;
	
							end++;
							t1 = allWords[i].substring(0, end+1);
							t2 = allWords[curr.substr.wordIndex].substring(0, end+1);
							prefixI = new Indexes(curr.substr.wordIndex, (short)0, end);
						}
					}
	
				}else{
					t1 = allWords[i].substring(curr.substr.startIndex, curr.substr.endIndex+1);
					t2 = allWords[curr.substr.wordIndex].substring(curr.substr.startIndex, curr.substr.endIndex+1);
	
					//if the prefix is the same for the new word(compare w curr.firstchild)
					if(t1.equals(t2)){
						Indexes tI = new Indexes(i, (short)(curr.substr.endIndex+1), (short)(allWords[i].length()-1));
						TrieNode tN = new TrieNode(tI, null, null);
						TrieNode header = curr.firstChild;
						
						curr.firstChild = addToEnd(header, tN);

						//compare additional prefix w firstChild(compare w siblings of curr.firstchild)
						header = curr.firstChild;

						t1="a";
						t2="b";
						int c = 0;
						while(!t1.equals(t2) && header!=tN){
							int diff = header.substr.endIndex-header.substr.startIndex;
			
							if(diff==0){
								t1 = allWords[i].substring(header.substr.startIndex, header.substr.endIndex+1);
								t2 = allWords[header.substr.wordIndex].substring(header.substr.startIndex, header.substr.endIndex+1);
							}else{
								if(header.substr.endIndex<allWords[i].length()){
									t1 = allWords[i].substring(header.substr.startIndex, header.substr.endIndex);
									t2 = allWords[header.substr.wordIndex].substring(header.substr.startIndex, header.substr.endIndex);
								}
							}

							if(t1.equals(t2)){
								//create new node for the new word which is the sibling
								Indexes nI = new Indexes(i, (short)(header.substr.endIndex), (short)(allWords[i].length()-1));
								TrieNode nN = new TrieNode(nI, null, null);
	
								//create new node for the new firstchild
								Indexes oI = new Indexes(header.substr.wordIndex, (short)(header.substr.endIndex), header.substr.endIndex);
								TrieNode oN = new TrieNode(oI, header.firstChild, nN);
	
								//create the new curr/root.firstchild
								header = removeFromEnd(header);
								Indexes tempI = new Indexes(header.substr.wordIndex, header.substr.startIndex, (short)(header.substr.endIndex-1));
								TrieNode tempN = new TrieNode(tempI, oN, header.sibling);
	
								header = tempN;
								curr.firstChild = find(curr.firstChild, c, header);
							}

							if(!t1.equals(t2)){
								int shorten = -1;
								diff = (header.substr.endIndex+shorten)-header.substr.startIndex;
								while(!t1.equals(t2) && diff>0){
									if((header.substr.endIndex+shorten)>header.substr.startIndex){
										t1 = allWords[i].substring(header.substr.startIndex, header.substr.endIndex+shorten);
										t2 = allWords[header.substr.wordIndex].substring(header.substr.startIndex, header.substr.endIndex+shorten);
										shorten--;
										diff = (header.substr.endIndex+shorten)-header.substr.startIndex;
									}else{break;}
								}
								
								if(t1.equals(t2)){
									//create new node for the new word which is the sibling
									Indexes nI = new Indexes(i, (short)(header.substr.endIndex+shorten+1), (short)(allWords[i].length()-1));
									TrieNode nN = new TrieNode(nI, null, null);
		
									//create new node for the new firstchild
									Indexes oI = new Indexes(header.substr.wordIndex, (short)(header.substr.endIndex+shorten+1), header.substr.endIndex);
									TrieNode oN = new TrieNode(oI, header.firstChild, nN);
		
									//create the new curr/root.firstchild
									header = removeFromEnd(header);

									Indexes tempI = new Indexes(header.substr.wordIndex, header.substr.startIndex, (short)(header.substr.endIndex+shorten));
									TrieNode tempN = new TrieNode(tempI, oN, header.sibling);
		
									if(header==curr.firstChild){
										header = tempN;
										curr.firstChild=header;
									}else{
										header = tempN;
										curr.firstChild = removeFromEnd(curr.firstChild);
										curr.firstChild = addToEnd(curr.firstChild, header);
									}
									
								}

							}
							header=header.sibling;
							c++;
						}

					}else{
						int shorten = 0;
						while(!t1.equals(t2)){
							t1 = allWords[i].substring(curr.substr.startIndex, curr.substr.endIndex+shorten);
							t2 = allWords[curr.substr.wordIndex].substring(curr.substr.startIndex, curr.substr.endIndex+shorten);
							shorten--;
						}

						if(t1.equals(t2)){
							//create new node for the new word which is the sibling
							Indexes nI = new Indexes(i, (short)(curr.substr.endIndex+shorten+1), (short)(allWords[i].length()-1));
							TrieNode nN = new TrieNode(nI, null, null);

							//create new node for the new firstchild
							Indexes oI = new Indexes(curr.substr.wordIndex, (short)(curr.substr.endIndex+shorten+1), curr.substr.endIndex);
							TrieNode oN = new TrieNode(oI, curr.firstChild, nN);

							//create the new curr/root.firstchild
							Indexes tempI = new Indexes(curr.substr.wordIndex, curr.substr.startIndex, (short)(curr.substr.endIndex+shorten));
							TrieNode tempN = new TrieNode(tempI, oN, curr.sibling);

							curr = tempN;
						}

					}
					
				}

			}

			end=0;
		}
		
		
		
		root.firstChild = curr;

		return root;
	}

	private static TrieNode find(TrieNode headNode, int position, TrieNode target){
		TrieNode head = headNode;

		// head node is changing.
		if (position == 0) {
			head = target;
		} else {
			while (position-- != 0) {
				if (position == 1) {
					// Replacing current with new Node
					// to the old Node to point to the new Node
					headNode.sibling = target;
					break;
				}
				headNode = headNode.sibling;
			}
		}
		return head;
	}

	private static TrieNode addToEnd(TrieNode header, TrieNode newNode){
		TrieNode ret = header;

       // loop until we find the end of the list
       while ((header.sibling != null)) {
           header = header.sibling;
       }

       // set the new node
       header.sibling = newNode;
       return ret;
	}

	private static TrieNode removeFromEnd(TrieNode head){
		TrieNode second_last = head; 

		if(head == null){
			return null; 
		} 
			
		if(head.sibling == null) { 
			return null; 
		} 
        while (second_last.sibling.sibling != null) 
            second_last = second_last.sibling; 
  
        // Change next of second last 
        second_last.sibling = null; 
  
        return head;
	}

	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		
		ArrayList<TrieNode> a = new ArrayList<TrieNode>();
		TrieNode ptr = root.firstChild;

			ptr = findPrefix(ptr, prefix, allWords);
			if(ptr==null){
				return null;
			}else{
				printLeafNodes(ptr.firstChild, a);
				if(a.size()==0){
					a.add(ptr);
				}
			}
		return a;
	}

	private static TrieNode findPrefix(TrieNode ptr, String prefix, String[] allWords){

		//base case1
		if(ptr==null)return ptr;

		//if it matches w prefix & if end index is > length of prefix
		String s1 = "zyx", s2 = prefix;
		
		if((ptr.substr.endIndex+1)>prefix.length()){
			return findPrefix(ptr.sibling, prefix, allWords);
		}else{
			s1 = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex+1);
			s2 = prefix.substring(ptr.substr.startIndex, ptr.substr.endIndex+1);

			if(s1.equals(s2) && (ptr.substr.endIndex+1)==prefix.length()){
				return ptr;
			}
			if(s1.equals(s2)){
				return findPrefix(ptr.firstChild, prefix, allWords);
			}
		}

	
		return findPrefix(ptr.sibling, prefix, allWords);

	}

	private static void printLeafNodes(TrieNode ptr, ArrayList<TrieNode> a){
		//check if root is null
		if(ptr==null){
			return;
		}

		//leaf node
		if(ptr.firstChild==null){
			a.add(ptr);
		}else{
			printLeafNodes(ptr.firstChild, a);
		}
		
		printLeafNodes(ptr.sibling, a);
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
