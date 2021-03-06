package edu.uci.ics.githubuserskills.ranking.export.strategy;

import edu.uci.ics.githubuserskills.profile.UserDomainRanking;

/**
 * @author matias
 *
 */
public class FilterEmptyRankingsStrategy implements RankingExportStrategy {

	@Override
	public boolean exportable(UserDomainRanking ranking) {
		return !ranking.getTerms().isEmpty();
	}

}
