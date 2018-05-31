package p3MainClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import queues.Queue;
import queues.SLLQueue;
import treeClasses.LinkedTree;
import treeClasses.Position;

public class GenealogicalTreeMain {
	
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
			
			//Create original tree.
			LinkedTree<Integer> bigTree = new LinkedTree<>();
			for(int i=0; i<data.size(); i+=2) {
				if(bigTree.isEmpty()) {
					bigTree.addRoot(data.get(i));
					bigTree.addChild(bigTree.root(), data.get(i+1));
				} else {
					Position<Integer> tempPos = null;
					for(Position<Integer> pos : bigTree.positions()) {
						if(data.get(i) == pos.getElement()) {
							//If a position in the subtree is equal to the current parent, then save that position temporarily.
							tempPos = pos;
						}
					}
					if(tempPos != null) {
						bigTree.addChild(tempPos, data.get(i+1));
					}
				}
			}
			System.out.println("Original tree:");
			bigTree.display();
			
			//Begin creating subtrees.
			ArrayList<LinkedTree<Integer>> subtrees = new ArrayList<>();
			
			Queue<Position<Integer>> bfq = new SLLQueue<>();
			bfq.enqueue(bigTree.root());
			while(!bfq.isEmpty()) {
				LinkedTree<Integer> subtree = new LinkedTree<>();
				Position<Integer> pos = bfq.dequeue();
				int gender = pos.getElement() % 2;
				Position<Integer> otherPos = subtree.addRoot(pos.getElement());
				genderSubtree(gender, bigTree, pos, subtree, otherPos); 
				for(Position<Integer> child : bigTree.children(pos)) {
					if(!bigTree.isExternal(child)) {
						bfq.enqueue(child);
					}
				}
				subtrees.add(subtree);
			}
			//Display subtrees.
			for(int i=0; i<subtrees.size(); i++) {
				System.out.println("Subtree " + (i+1) + ":");
				subtrees.get(i).display();
			}
		}catch (FileNotFoundException e) {
			System.out.println("Directory or file not found!");
		}
		
	}
	
	private static void genderSubtree(int gender, LinkedTree<Integer> original, Position<Integer> origRoot, LinkedTree<Integer> other,
			Position<Integer> otherRoot) {
		for (Position<Integer> pThis : original.children(origRoot)) { 
			if(pThis.getElement() % 2 == gender) {
				Position<Integer> pOther = other.addChild(otherRoot, pThis.getElement());
				genderSubtree(gender, original, pThis, other, pOther); 
			} else {
				other.addChild(otherRoot, pThis.getElement());
			}
		}
	}
	
}
