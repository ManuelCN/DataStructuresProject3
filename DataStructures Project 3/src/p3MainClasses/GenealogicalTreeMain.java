package p3MainClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import treeClasses.LinkedTree;
import treeClasses.Position;

public class GenealogicalTreeMain {

	private static LinkedTree<Integer>[] trees;
	
	public static void main(String[] args) {
		try {
			//Begin reading data from file.
			Scanner inputFile = new Scanner(new File("programFiles", "trees.txt"));
			ArrayList<Integer> data = new ArrayList<>();
			while(inputFile.hasNext()) {
				data.add(inputFile.nextInt());
				data.add(inputFile.nextInt());
			}
			inputFile.close();
			//Finish reading data from file.
			System.out.println(data);	//Display obtained data.
			trees = new LinkedTree[data.size()/2];	//Initializes the private tree array with the theoretical amount of subtrees. [REVISA ESTO BRUH]
			//Being creating subtrees.
			int start = 0;	//Where in the data ArrayList the subtree construction will begin.
			for(int i=0; i<trees.length; i++) {
				int gender = 0;	//The gender of the root of the subtree.
				LinkedTree<Integer> temp = new LinkedTree<>();
				//From "start" until the end of the ArrayList.
				for(int j=start; j<data.size(); j+=2) {
					//If subtree is empty, automatically fill with first position.
					//This will determine the gender of all internal nodes.
					//Will work with the parent-child relationship pairs (uneven will always be a parent, even will always be the child).
					if(temp.isEmpty()) {
						temp.addRoot(data.get(j));
						gender = data.get(j) % 2;	//0 for Men, 1 for Women
						temp.addChild(temp.root(), data.get(j+1));
						
					} else {
						//If the subtree is no longer empty, continue construction with initialized parameters (gender, who is root, etc).
						Position<Integer> tempPos = null;
						for(Position<Integer> pos : temp.positions()) {
							if(data.get(j) == pos.getElement()) {
								//If a position in the subtree is equal to the current parent, then save that position temporarily.
								tempPos = pos;
							}
						}
						if(tempPos != null) {
							if(tempPos.getElement() % 2 == gender) {
								//If a parent is found and the child is of the appropriate gender, add to subtree.
								temp.addChild(tempPos, data.get(j+1));
							}
						}
					}
				}
				//Save constructed subtree in the array.
				trees[i] = temp;
				//Increase the starting index of the data ArrayList by 2 (next parent-child relationship pair).
				start += 2;
			}
			//Display the subtrees.
			for(int i=0; i<trees.length; i++) {
				System.out.println("Sub-tree " + (i+1) + ":");
				trees[i].display();
			}
		}catch (FileNotFoundException e) {
			System.out.println("Directory or file not found!");
		}
		
	}
	
}
