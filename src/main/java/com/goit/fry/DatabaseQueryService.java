package com.goit.fry;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseQueryService {

	private static final Logger logger = LogManager.getRootLogger();
	private final SQLFile sql_file;

	public DatabaseQueryService() {

		sql_file = new SQLFile(logger);
		sql_file.addDMLSelectPattern();
	}

	private void executeAndGetResult(String func_name, String sql_file_path,
									 IQueryResult result_container) {

		try {
			String query = sql_file.loadCommand(sql_file_path);
			sql_file.executeCommand(query, result_container);
		}
		catch (Exception e) {

			logger.error(func_name + " failed: " + e);
		}
	}

	public List<LongestProject> findLongestProject() {

		logger.info("executing findLongestProject()");
		LongestProjectResult result_container = new LongestProjectResult();
		executeAndGetResult("findLongestProject()",
							"sql/find_longest_project.sql", result_container);

		return result_container.getResult();
	}

	public List<MaxProjectsClient> findMaxProjectsClient() {

		logger.info("executing findMaxProjectsClient()");
		MaxProjectsClientResult result_container = new MaxProjectsClientResult();
		executeAndGetResult("findMaxProjectsClient()",
							"sql/find_max_projects_client.sql", result_container);

		return result_container.getResult();
	}

	public List<MaxSalaryWorker> findMaxSalaryWorker() {

		logger.info("executing findMaxSalaryWorker()");
		MaxSalaryWorkerResult result_container = new MaxSalaryWorkerResult();
		executeAndGetResult("findMaxSalaryWorker()",
							"sql/find_max_salary_worker.sql", result_container);

		return result_container.getResult();
	}

	public List<YoungestEldestWorkers> findYoungestEldestWorkers() {

		logger.info("executing findYoungestEldestWorkers()");
		YoungestEldestWorkersResult result_container = new YoungestEldestWorkersResult();
		executeAndGetResult("findYoungestEldestWorkers()",
							"sql/find_youngest_eldest_workers.sql", result_container);

		return result_container.getResult();
	}

	public List<ProjectPrices> printProjectPrices() {

		logger.info("executing printProjectPrices()");
		ProjectPricesResult result_container = new ProjectPricesResult();
		executeAndGetResult("printProjectPrices()",
							"sql/print_project_prices.sql", result_container);

		return result_container.getResult();
	}
}
