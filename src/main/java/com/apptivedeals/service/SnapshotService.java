package com.apptivedeals.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apptivedeals.dao.SnapshotDao;
import com.apptivedeals.entity.Snapshot;

@Service
public class SnapshotService {
	
	@Autowired
	private SnapshotDao snapshotDao;
	
	private Snapshot latestSnapshot = null;
	
	@PostConstruct
	public void init() {
		latestSnapshot = snapshotDao.getLatestSnapshot();
	}
	
	public void setLatestSnapshot(Snapshot latestSnapshot) {
		this.latestSnapshot = latestSnapshot;
	}
	
	public Snapshot getLatestSnapshot() {
		return this.latestSnapshot;
	}
	
	public Long getLatestSnapshotId() {
		Long latestSnapshotId = null;
		if (this.latestSnapshot != null) {
			latestSnapshotId = this.latestSnapshot.id;
		}
		
		return latestSnapshotId;
	}
	
	public Snapshot getSnapshot(Long id) {
		return snapshotDao.getSnapshot(id);
	}
}
