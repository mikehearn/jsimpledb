
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 */

package org.jsimpledb.cli.cmd;

import java.util.Map;

import org.jsimpledb.cli.CliSession;
import org.jsimpledb.kv.raft.RaftKVTransaction;
import org.jsimpledb.parse.ParseContext;

@Command
public class RaftAddCommand extends AbstractRaftCommand {

    public RaftAddCommand() {
        super("raft-add node address");
    }

    @Override
    public String getHelpSummary() {
        return "adds a node to a Raft clustered database";
    }

    @Override
    public CliSession.Action getAction(CliSession session, ParseContext ctx, boolean complete, Map<String, Object> params) {
        final String node = (String)params.get("node");
        final String address = (String)params.get("address");
        return new RaftAction() {
            @Override
            protected void run(CliSession session, RaftKVTransaction tx) throws Exception {
                tx.configChange(node, address);
            }
        };
    }
}
