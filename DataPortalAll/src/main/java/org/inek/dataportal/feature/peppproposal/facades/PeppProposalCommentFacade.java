package org.inek.dataportal.feature.peppproposal.facades;

import org.inek.dataportal.common.data.AbstractFacade;
import javax.ejb.Stateless;
import javax.inject.Named;
import org.inek.dataportal.feature.peppproposal.entities.PeppProposalComment;

/**
 *
 * @author muellermi
 */
@Stateless
public class PeppProposalCommentFacade extends AbstractFacade<PeppProposalComment> {

    public PeppProposalCommentFacade() {
        super(PeppProposalComment.class);
    }

}
