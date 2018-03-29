package simpledb.materialize;

import java.util.ArrayList;
import java.util.List;

import simpledb.query.Plan;
import simpledb.query.Scan;
import simpledb.query.UpdateScan;
import simpledb.record.Schema;
import simpledb.tx.Transaction;


///DAVIDE
public class NWaysSortPlan {

	private Plan p;
	private Transaction tx;
	private Schema sch;
	private RecordComparator comp;
	private SortPlan sortp;
	private int kRuns;


	public NWaysSortPlan(Plan p, List<String> sortfields, Transaction tx, int runs) {
		this.p = p;
		this.tx = tx;
		this.sch = p.schema();
		this.comp = new RecordComparator(sortfields);
		this.sortp = new SortPlan(p, sortfields, tx);
		this.kRuns = runs;
	}



	public Scan open() {
		Scan src = p.open();
		List<TempTable> runs = this.sortp.splitIntoRuns(src, this.kRuns );
		src.close();
		while (runs.size() > 2)
			runs = doAMergeIteration(runs);
		return (Scan) new NWaysSortScan(runs, comp);
	}



	private List<TempTable> doAMergeIteration(List<TempTable> runs) {
		List<TempTable> result = new ArrayList<TempTable>();
		while (runs.size() > 1) {
			TempTable p1 = runs.remove(0);
			TempTable p2 = runs.remove(0);
			result.add(mergeRuns(p1, p2));
		}
		if (runs.size() == 1)
			result.add(runs.get(0));
		return result;
	}


	//mergeTwoRuns di sortplan
	private TempTable mergeRuns(TempTable p1, TempTable p2) {
		Scan src1 = p1.open();
		Scan src2 = p2.open();
		TempTable result = new TempTable(sch, tx);
		UpdateScan dest = result.open();

		boolean hasmore1 = src1.next();
		boolean hasmore2 = src2.next();
		while (hasmore1 && hasmore2)
			if (comp.compare(src1, src2) < 0)
				hasmore1 = copy(src1, dest);
			else
				hasmore2 = copy(src2, dest);

		if (hasmore1)
			while (hasmore1)
				hasmore1 = copy(src1, dest);
		else
			while (hasmore2)
				hasmore2 = copy(src2, dest);
		src1.close();
		src2.close();
		dest.close();
		return result;
	}


	private boolean copy(Scan src, UpdateScan dest) {
		dest.insert();
		for (String fldname : sch.fields())
			dest.setVal(fldname, src.getVal(fldname));
		return src.next();
	}




}
