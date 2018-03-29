package simpledb.materialize;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.xml.soap.SAAJResult;

import simpledb.query.Constant;
import simpledb.query.UpdateScan;
import simpledb.record.RID;

//DAVIDE
public class NWaysSortScan {


	private Map<Integer,UpdateScan> scans;
	private UpdateScan currentscan;
	private RecordComparator comp;
	private List<RID> savedposition;



	public NWaysSortScan(List<TempTable> runs, RecordComparator comp) {
		this.comp = comp;
		//		s1 = (UpdateScan) runs.get(0).open();
		//		hasmore1 = s1.next();
		//		if (runs.size() > 1) {
		//			s2 = (UpdateScan) runs.get(1).open();
		//			hasmore2 = s2.next();
		//		}

		Integer i = 0;
		for(TempTable t : runs) {
			scans.put(i,t.open());
			i++;
		}
	}


	public void beforeFirst() {
		currentscan = null;
		//	      s1.beforeFirst();
		//	      hasmore1 = s1.next();
		//	      if (s2 != null) {
		//	         s2.beforeFirst();
		//	         hasmore2 = s2.next();
		//	      }
		for(UpdateScan s : scans.values()){
			s.beforeFirst();
		}
	}



	/**
	 * Moves to the next record in sorted order.
	 * First, the current scan is moved to the next record.
	 * Then the lowest record of the two scans is found, and that
	 * scan is chosen to be the new current scan.
	 * @see simpledb.query.Scan#next()
	 */
	public boolean next() {
		//		if (currentscan != null) {
		//			if (currentscan == s1)
		//				hasmore1 = s1.next();
		//			else if (currentscan == s2)
		//				hasmore2 = s2.next();
		//		}
		//
		//		if (!hasmore1 && !hasmore2)
		//			return false;
		//		else if (hasmore1 && hasmore2) {
		//			if (comp.compare(s1, s2) < 0)
		//				currentscan = s1;
		//			else
		//				currentscan = s2;
		//		}
		//		else if (hasmore1)
		//			currentscan = s1;
		//		else if (hasmore2)
		//			currentscan = s2;
		//		return true;


		List<Boolean> bol = new ArrayList<>();

		for(Integer i : scans.keySet()) {
			bol.add(bolnext(scans.get(i)));
		}

		if(bol.stream().allMatch(i->i==false))
			return false;
		else if(bol.stream().filter(i->i==true).count() > 1){

			List<UpdateScan> tmp = new ArrayList<>();
			for(UpdateScan s : scans.values()) {
				if(bolnext(s))
					tmp.add(s);
			}
			currentscan = lessScan(tmp);
		}
		else if(bol.stream().filter(i->i==true).count() == 1) {

			for(UpdateScan s : scans.values()) {
				if(bolnext(s))
					currentscan = s;
			}
		}
		//		
		//		currentscan = lessScan(scans);
		//		}
		//		return true;


		return true;

	}



	private UpdateScan lessScan(List<UpdateScan> tmp) {


		UpdateScan s = tmp.stream().min(comp).orElseThrow(NoSuchElementException::new);

		return s;
	}


	private boolean bolnext(UpdateScan updateScan) {

		if(updateScan.next() == false)
			return false;

		return true;
	}


	public void close() {
		for(UpdateScan s : scans.values())
			s.close();
	}




	/**
	 * Gets the Constant value of the specified field
	 * of the current scan.
	 * @see simpledb.query.Scan#getVal(java.lang.String)
	 */
	public Constant getVal(String fldname) {
		return currentscan.getVal(fldname);
	}

	/**
	 * Gets the integer value of the specified field
	 * of the current scan.
	 * @see simpledb.query.Scan#getInt(java.lang.String)
	 */
	public int getInt(String fldname) {
		return currentscan.getInt(fldname);
	}

	/**
	 * Gets the string value of the specified field
	 * of the current scan.
	 * @see simpledb.query.Scan#getString(java.lang.String)
	 */
	public String getString(String fldname) {
		return currentscan.getString(fldname);
	}

	/**
	 * Returns true if the specified field is in the current scan.
	 * @see simpledb.query.Scan#hasField(java.lang.String)
	 */
	public boolean hasField(String fldname) {
		return currentscan.hasField(fldname);
	}


	public void savePosition() {
		//		RID rid1 = s1.getRid();
		//		RID rid2 = (s2 == null) ? null : s2.getRid();
		//		savedposition = Arrays.asList(rid1,rid2);

		List<RID> r = new ArrayList<>();

		for(UpdateScan s : scans.values()) {
			r.add(s.getRid());
		}

		savedposition = r;
	}

	/**
	 * Moves the scan to its previously-saved position.
	 */
	public void restorePosition() {
		//		RID rid1 = savedposition.get(0);
		//		RID rid2 = savedposition.get(1);
		//		s1.moveToRid(rid1);
		//		if (rid2 != null)
		//			s2.moveToRid(rid2);
		//

		Iterator<RID> r = savedposition.iterator();
		Iterator<UpdateScan> s = scans.values().iterator();

		while(r.hasNext() && s.hasNext()) {
			s.next().moveToRid(r.next());
		}
	}
}
