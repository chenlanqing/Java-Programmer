<html>
<head>
    <meta charset="utf-8">
    <title>OPTIMIZER_TRACE详解</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
<body>
<div class="lessonwrap imageText" id="lessonwrap">
    <div class="pannel cont-pannel" id="cont-pannel">
        <span class="moco-tick hide"></span>
        <div class="cont-scrollbar ps-container" id="cont-scrollbar">
            <div class="scroller content" style="min-height: 790px;">
                <div class="cont-title-wrap wm">
                    <div class="cont-desc ueword">
                        <div class="cl-preview-section"><h1 id="optimizer_trace详解">OPTIMIZER_TRACE详解</h1>
                        </div>
                        <div class="cl-preview-section">
                            <blockquote>
                                <p><strong>TIPS</strong></p>
                                <p>本文基于MySQL 8.0编写，理论支持MySQL 5.6及更高版本。</p>
                            </blockquote>
                        </div>
                        <div class="cl-preview-section"><p>OPTIMIZER_TRACE是MySQL
                            5.6引入的一项跟踪功能，它可以跟踪优化器做出的各种决策（比如访问表的方法、各种开销计算、各种转换等），并将跟踪结果记录到 <code>INFORMATION_SCHEMA.OPTIMIZER_TRACE</code>
                            表中。此功能默认关闭，开启后，可分析如下语句：</p>
                        </div>
                        <div class="cl-preview-section">
                            <ul>
                                <li>SELECT</li>
                                <li>INSERT</li>
                                <li>REPLACE</li>
                                <li>UPDATE</li>
                                <li>DELETE</li>
                                <li>EXPLAIN</li>
                                <li>SET</li>
                                <li>DECLARE</li>
                                <li>CASE</li>
                                <li>IF</li>
                                <li>RETURN</li>
                                <li>CALL</li>
                            </ul>
                        </div>
                        <div class="cl-preview-section"><h2 id="optimizer_trace相关参数">OPTIMIZER_TRACE相关参数</h2>
                        </div>
                        <div class="cl-preview-section">
                            <blockquote>
                                <p><strong>TIPS</strong></p>
                                <p>参考 <a
                                        href="https://dev.mysql.com/doc/internals/en/system-variables-controlling-trace.html">https://dev.mysql.com/doc/internals/en/system-variables-controlling-trace.html</a>
                                </p>
                            </blockquote>
                        </div>
                        <div class="cl-preview-section">
                            <ul>
                                <li>
                                    <p>optimizer_trace</p>
                                    <ul>
                                        <li>optimizer_trace总开关，默认值：<code>enabled=off,one_line=off</code></li>
                                        <li>enabled：是否开启optimizer_trace；on表示开启，off表示关闭。</li>
                                        <li>
                                            one_line：是否开启单行存储。on表示开启；off表示关闭，将会用标准的JSON格式化存储。设置成on将会有良好的格式，设置成off可节省一些空间。
                                        </li>
                                    </ul>
                                </li>
                                <li>
                                    <p>optimizer_trace_features</p>
                                    <ul>
                                        <li>控制optimizer_trace跟踪的内容，默认值：<code>greedy_search=on,range_optimizer=on,dynamic_range=on,repeated_subselect=on</code>
                                            ，表示开启所有跟踪项。
                                        </li>
                                    </ul>
                                </li>
                                <li>
                                    <p>greedy_search：是否跟踪贪心搜索，有关贪心算法详见 <a
                                            href="https://blog.csdn.net/weixin_42813521/article/details/105563103">https://blog.csdn.net/weixin_42813521/article/details/105563103</a>
                                    </p>
                                    <ul>
                                        <li>range_optimizer：是否跟踪范围优化器</li>
                                    </ul>
                                </li>
                                <li>
                                    <p>dynamic_range：是否跟踪动态范围优化</p>
                                    <ul>
                                        <li>repeated_subselect：是否跟踪子查询，如果设置成off，只跟踪第一条Item_subselect的执行</li>
                                    </ul>
                                </li>
                                <li>
                                    <p>详见 <a
                                            href="https://dev.mysql.com/doc/internals/en/optimizer-features-to-trace.html">https://dev.mysql.com/doc/internals/en/optimizer-features-to-trace.html</a>
                                    </p>
                                </li>
                                <li>
                                    <p>optimizer_trace_limit：控制optimizer_trace展示多少条结果，默认1</p>
                                </li>
                                <li>
                                    <p>optimizer_trace_max_mem_size：optimizer_trace堆栈信息允许的最大内存，默认1048576</p>
                                </li>
                                <li>
                                    <p>optimizer_trace_offset：第一个要展示的optimizer trace的偏移量，默认-1。</p>
                                </li>
                                <li>
                                    <p>
                                        end_markers_in_json：如果JSON结构很大，则很难将右括号和左括号配对。为了帮助读者阅读，可将其设置成on，这样会在右括号附近加上注释，默认off。</p>
                                    <blockquote>
                                        <p>参考： <a
                                                href="https://dev.mysql.com/doc/internals/en/end-markers-in-json-system-variable.html">https://dev.mysql.com/doc/internals/en/end-markers-in-json-system-variable.html</a>
                                        </p>
                                    </blockquote>
                                </li>
                            </ul>
                        </div>
                        <div class="cl-preview-section">
                            <blockquote>
                                <p><strong>TIPS</strong></p>
                                <ul>
                                    <li>
                                        <p>以上参数可用SET语句操作，例如，用如下命令即可打开OPTIMIZER TRACE</p>
                                        <pre class=" language-sql"><code class="prism  language-sql"><span
                                                class="token keyword">SET</span> OPTIMIZER_TRACE<span
                                                class="token operator">=</span><span
                                                class="token string">"enabled=on"</span><span class="token punctuation">,</span>END_MARKERS_IN_JSON<span
                                                class="token operator">=</span><span
                                                class="token keyword">on</span><span class="token punctuation">;</span>
</code></pre>
                                        <p>也可用SET GLOBAL全局开启。但即使全局开启OPTIMIZER_TRACE，每个Session也只能跟踪它自己执行的语句：</p>
                                        <pre class=" language-sql"><code class="prism  language-sql"><span
                                                class="token keyword">SET</span> <span
                                                class="token keyword">GLOBAL</span> OPTIMIZER_TRACE<span
                                                class="token operator">=</span><span
                                                class="token string">"enabled=on"</span><span class="token punctuation">,</span>END_MARKERS_IN_JSON<span
                                                class="token operator">=</span><span
                                                class="token keyword">on</span><span class="token punctuation">;</span>
</code></pre>
                                    </li>
                                    <li>
                                        <p>optimizer_trace_limit和optimizer_trace_offset这两个参数经常配合使用，例如：</p>
                                        <pre class=" language-sql"><code class="prism  language-sql"><span
                                                class="token keyword">SET</span> optimizer_trace_offset<span
                                                class="token operator">=</span><span
                                                class="token operator">&lt;</span><span
                                                class="token keyword">OFFSET</span><span
                                                class="token operator">&gt;</span><span
                                                class="token punctuation">,</span> optimizer_trace_limit<span
                                                class="token operator">=</span><span
                                                class="token operator">&lt;</span><span
                                                class="token keyword">LIMIT</span><span
                                                class="token operator">&gt;</span>
</code></pre>
                                        <p>这两个参数配合使用，有点类似MySQL里面的 limit语句。</p>
                                        <p>
                                            默认情况下，由于optimizer_trace_offset=-1，optimizer_trace_limit=1，记录最近的一条SQL语句，展示时，每次展示1条数据；</p>
                                        <p>如果改成 <code>SET optimizer_trace_offset=-2, optimizer_trace_limit=1</code>
                                            ，则会记录倒数第二条SQL语句；</p>
                                        <p>有关 optimizer_trace_offset 、optimizer_trace_limit更多细节，可参考 <a
                                                href="https://dev.mysql.com/doc/internals/en/tuning-trace-purging.html">https://dev.mysql.com/doc/internals/en/tuning-trace-purging.html</a>
                                        </p>
                                    </li>
                                </ul>
                            </blockquote>
                        </div>
                        <div class="cl-preview-section"><h2 id="optimizer_trace使用">OPTIMIZER_TRACE使用</h2>
                        </div>
                        <div class="cl-preview-section">
                            <ul>
                                <li>
                                    <p>开启OPTIMIZER_TRACE功能，并设置要展示的数据条目数：</p>
                                    <pre class=" language-sql"><code class="prism  language-sql"><span
                                            class="token keyword">SET</span> OPTIMIZER_TRACE<span
                                            class="token operator">=</span><span
                                            class="token string">"enabled=on"</span><span
                                            class="token punctuation">,</span>END_MARKERS_IN_JSON<span
                                            class="token operator">=</span><span class="token keyword">on</span><span
                                            class="token punctuation">;</span>
<span class="token keyword">SET</span> optimizer_trace_offset<span class="token operator">=</span><span
                                                class="token operator">-</span><span class="token number">30</span><span
                                                class="token punctuation">,</span> optimizer_trace_limit<span
                                                class="token operator">=</span><span class="token number">30</span><span
                                                class="token punctuation">;</span>
</code></pre>
                                </li>
                                <li>
                                    <p>发送你想要分析的SQL语句，例如：</p>
                                    <pre class=" language-sql"><code class="prism  language-sql"><span
                                            class="token keyword">select</span> <span class="token operator">*</span>
<span class="token keyword">from</span> salaries
<span class="token keyword">where</span> from_date <span class="token operator">=</span> <span class="token string">'1986-06-26'</span>
  <span class="token operator">and</span> to_date <span class="token operator">=</span> <span class="token string">'1987-06-26'</span><span
                                                class="token punctuation">;</span>
</code></pre>
                                </li>
                                <li>
                                    <p>使用如下语句分析，即可获得类似如下的结果：</p>
                                    <pre class=" language-sql"><code class="prism  language-sql">mysql<span
                                            class="token operator">&gt;</span> <span class="token keyword">SELECT</span> <span
                                            class="token operator">*</span> <span class="token keyword">FROM</span> INFORMATION_SCHEMA<span
                                            class="token punctuation">.</span>OPTIMIZER_TRACE <span
                                            class="token keyword">limit</span> <span
                                            class="token number">30</span> \G<span class="token punctuation">;</span>
<span class="token operator">*</span><span class="token operator">*</span><span class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span> <span class="token number">1</span><span
                                                class="token punctuation">.</span> <span
                                                class="token keyword">row</span> <span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span
                                                class="token operator">*</span><span class="token operator">*</span>
                          QUERY: <span class="token keyword">select</span> <span class="token operator">*</span>
<span class="token keyword">from</span> salaries
<span class="token keyword">where</span> from_date <span class="token operator">=</span> <span class="token string">'1986-06-26'</span>
  <span class="token operator">and</span> to_date <span class="token operator">=</span> <span class="token string">'1987-06-26'</span>
                            TRACE: {
  <span class="token string">"steps"</span>: <span class="token punctuation">[</span>
    {
      <span class="token string">"join_preparation"</span>: {
        <span class="token string">"select#"</span>: <span class="token number">1</span><span class="token punctuation">,</span>
        <span class="token string">"steps"</span>: <span class="token punctuation">[</span>
          {
            <span class="token string">"expanded_query"</span>: <span class="token string">"/* select#1 */ select `salaries`.`emp_no` AS `emp_no`,`salaries`.`salary` AS `salary`,`salaries`.`from_date` AS `from_date`,`salaries`.`to_date` AS `to_date` from `salaries` where ((`salaries`.`from_date` = '1986-06-26') and (`salaries`.`to_date` = '1987-06-26'))"</span>
          }
        <span class="token punctuation">]</span> <span class="token comment">/* steps */</span>
      } <span class="token comment">/* join_preparation */</span>
    }<span class="token punctuation">,</span>
    {
      <span class="token string">"join_optimization"</span>: {
        <span class="token string">"select#"</span>: <span class="token number">1</span><span class="token punctuation">,</span>
        <span class="token string">"steps"</span>: <span class="token punctuation">[</span>
          {
            <span class="token string">"condition_processing"</span>: {
              <span class="token string">"condition"</span>: <span class="token string">"WHERE"</span><span
                                                class="token punctuation">,</span>
              <span class="token string">"original_condition"</span>: <span class="token string">"((`salaries`.`from_date` = '1986-06-26') and (`salaries`.`to_date` = '1987-06-26'))"</span><span
                                                class="token punctuation">,</span>
              <span class="token string">"steps"</span>: <span class="token punctuation">[</span>
                {
                  <span class="token string">"transformation"</span>: <span
                                                class="token string">"equality_propagation"</span><span
                                                class="token punctuation">,</span>
                  <span class="token string">"resulting_condition"</span>: <span class="token string">"(multiple equal('1986-06-26', `salaries`.`from_date`) and multiple equal('1987-06-26', `salaries`.`to_date`))"</span>
                }<span class="token punctuation">,</span>
                {
                  <span class="token string">"transformation"</span>: <span
                                                class="token string">"constant_propagation"</span><span
                                                class="token punctuation">,</span>
                  <span class="token string">"resulting_condition"</span>: <span class="token string">"(multiple equal('1986-06-26', `salaries`.`from_date`) and multiple equal('1987-06-26', `salaries`.`to_date`))"</span>
                }<span class="token punctuation">,</span>
                {
                  <span class="token string">"transformation"</span>: <span class="token string">"trivial_condition_removal"</span><span
                                                class="token punctuation">,</span>
                  <span class="token string">"resulting_condition"</span>: <span class="token string">"(multiple equal(DATE'1986-06-26', `salaries`.`from_date`) and multiple equal(DATE'1987-06-26', `salaries`.`to_date`))"</span>
                }
              <span class="token punctuation">]</span> <span class="token comment">/* steps */</span>
            } <span class="token comment">/* condition_processing */</span>
          }<span class="token punctuation">,</span>
          {
            <span class="token string">"substitute_generated_columns"</span>: {
            } <span class="token comment">/* substitute_generated_columns */</span>
          }<span class="token punctuation">,</span>
          {
            <span class="token string">"table_dependencies"</span>: <span class="token punctuation">[</span>
              {
                <span class="token string">"table"</span>: <span class="token string">"`salaries`"</span><span
                                                class="token punctuation">,</span>
                <span class="token string">"row_may_be_null"</span>: <span class="token boolean">false</span><span
                                                class="token punctuation">,</span>
                <span class="token string">"map_bit"</span>: <span class="token number">0</span><span
                                                class="token punctuation">,</span>
                <span class="token string">"depends_on_map_bits"</span>: <span class="token punctuation">[</span>
                <span class="token punctuation">]</span> <span class="token comment">/* depends_on_map_bits */</span>
              }
            <span class="token punctuation">]</span> <span class="token comment">/* table_dependencies */</span>
          }<span class="token punctuation">,</span>
          {
            <span class="token string">"ref_optimizer_key_uses"</span>: <span class="token punctuation">[</span>
              {
                <span class="token string">"table"</span>: <span class="token string">"`salaries`"</span><span
                                                class="token punctuation">,</span>
                <span class="token string">"field"</span>: <span class="token string">"from_date"</span><span
                                                class="token punctuation">,</span>
                <span class="token string">"equals"</span>: <span class="token string">"DATE'1986-06-26'"</span><span
                                                class="token punctuation">,</span>
                <span class="token string">"null_rejecting"</span>: <span class="token boolean">false</span>
              }<span class="token punctuation">,</span>
              {
                <span class="token string">"table"</span>: <span class="token string">"`salaries`"</span><span
                                                class="token punctuation">,</span>
                <span class="token string">"field"</span>: <span class="token string">"to_date"</span><span
                                                class="token punctuation">,</span>
                <span class="token string">"equals"</span>: <span class="token string">"DATE'1987-06-26'"</span><span
                                                class="token punctuation">,</span>
                <span class="token string">"null_rejecting"</span>: <span class="token boolean">false</span>
              }
            <span class="token punctuation">]</span> <span class="token comment">/* ref_optimizer_key_uses */</span>
          }<span class="token punctuation">,</span>
          {
            <span class="token string">"rows_estimation"</span>: <span class="token punctuation">[</span>
              {
                <span class="token string">"table"</span>: <span class="token string">"`salaries`"</span><span
                                                class="token punctuation">,</span>
                <span class="token string">"range_analysis"</span>: {
                  <span class="token string">"table_scan"</span>: {
                    <span class="token string">"rows"</span>: <span class="token number">2838216</span><span
                                                class="token punctuation">,</span>
                    <span class="token string">"cost"</span>: <span class="token number">286799</span>
                  } <span class="token comment">/* table_scan */</span><span class="token punctuation">,</span>
                  <span class="token string">"potential_range_indexes"</span>: <span class="token punctuation">[</span>
                    {
                      <span class="token string">"index"</span>: <span class="token string">"PRIMARY"</span><span
                                                class="token punctuation">,</span>
                      <span class="token string">"usable"</span>: <span class="token boolean">false</span><span
                                                class="token punctuation">,</span>
                      <span class="token string">"cause"</span>: <span class="token string">"not_applicable"</span>
                    }<span class="token punctuation">,</span>
                    {
                      <span class="token string">"index"</span>: <span class="token string">"salaries_from_date_to_date_index"</span><span
                                                class="token punctuation">,</span>
                      <span class="token string">"usable"</span>: <span class="token boolean">true</span><span
                                                class="token punctuation">,</span>
                      <span class="token string">"key_parts"</span>: <span class="token punctuation">[</span>
                        <span class="token string">"from_date"</span><span class="token punctuation">,</span>
                        <span class="token string">"to_date"</span><span class="token punctuation">,</span>
                        <span class="token string">"emp_no"</span>
                      <span class="token punctuation">]</span> <span class="token comment">/* key_parts */</span>
                    }
                  <span class="token punctuation">]</span> <span
                                                class="token comment">/* potential_range_indexes */</span><span
                                                class="token punctuation">,</span>
                  <span class="token string">"setup_range_conditions"</span>: <span class="token punctuation">[</span>
                  <span class="token punctuation">]</span> <span
                                                class="token comment">/* setup_range_conditions */</span><span
                                                class="token punctuation">,</span>
                  <span class="token string">"group_index_range"</span>: {
                    <span class="token string">"chosen"</span>: <span class="token boolean">false</span><span
                                                class="token punctuation">,</span>
                    <span class="token string">"cause"</span>: <span
                                                class="token string">"not_group_by_or_distinct"</span>
                  } <span class="token comment">/* group_index_range */</span><span class="token punctuation">,</span>
                  <span class="token string">"skip_scan_range"</span>: {
                    <span class="token string">"potential_skip_scan_indexes"</span>: <span
                                                class="token punctuation">[</span>
                      {
                        <span class="token string">"index"</span>: <span class="token string">"salaries_from_date_to_date_index"</span><span
                                                class="token punctuation">,</span>
                        <span class="token string">"usable"</span>: <span class="token boolean">false</span><span
                                                class="token punctuation">,</span>
                        <span class="token string">"cause"</span>: <span class="token string">"query_references_nonkey_column"</span>
                      }
                    <span class="token punctuation">]</span> <span class="token comment">/* potential_skip_scan_indexes */</span>
                  } <span class="token comment">/* skip_scan_range */</span><span class="token punctuation">,</span>
                  <span class="token string">"analyzing_range_alternatives"</span>: {
                    <span class="token string">"range_scan_alternatives"</span>: <span
                                                class="token punctuation">[</span>
                      {
                        <span class="token string">"index"</span>: <span class="token string">"salaries_from_date_to_date_index"</span><span
                                                class="token punctuation">,</span>
                        <span class="token string">"ranges"</span>: <span class="token punctuation">[</span>
                          <span class="token string">"0xda840f &lt;= from_date &lt;= 0xda840f AND 0xda860f &lt;= to_date &lt;= 0xda860f"</span>
                        <span class="token punctuation">]</span> <span class="token comment">/* ranges */</span><span
                                                class="token punctuation">,</span>
                        <span class="token string">"index_dives_for_eq_ranges"</span>: <span
                                                class="token boolean">true</span><span
                                                class="token punctuation">,</span>
                        <span class="token string">"rowid_ordered"</span>: <span class="token boolean">true</span><span
                                                class="token punctuation">,</span>
                        <span class="token string">"using_mrr"</span>: <span class="token boolean">false</span><span
                                                class="token punctuation">,</span>
                        <span class="token string">"index_only"</span>: <span class="token boolean">false</span><span
                                                class="token punctuation">,</span>
                        <span class="token string">"rows"</span>: <span class="token number">86</span><span
                                                class="token punctuation">,</span>
                        <span class="token string">"cost"</span>: <span class="token number">50.909</span><span
                                                class="token punctuation">,</span>
                        <span class="token string">"chosen"</span>: <span class="token boolean">true</span>
                      }
                    <span class="token punctuation">]</span> <span
                                                class="token comment">/* range_scan_alternatives */</span><span
                                                class="token punctuation">,</span>
                    <span class="token string">"analyzing_roworder_intersect"</span>: {
                      <span class="token string">"usable"</span>: <span class="token boolean">false</span><span
                                                class="token punctuation">,</span>
                      <span class="token string">"cause"</span>: <span
                                                class="token string">"too_few_roworder_scans"</span>
                    } <span class="token comment">/* analyzing_roworder_intersect */</span>
                  } <span class="token comment">/* analyzing_range_alternatives */</span><span
                                                class="token punctuation">,</span>
                  <span class="token string">"chosen_range_access_summary"</span>: {
                    <span class="token string">"range_access_plan"</span>: {
                      <span class="token string">"type"</span>: <span class="token string">"range_scan"</span><span
                                                class="token punctuation">,</span>
                      <span class="token string">"index"</span>: <span class="token string">"salaries_from_date_to_date_index"</span><span
                                                class="token punctuation">,</span>
                      <span class="token string">"rows"</span>: <span class="token number">86</span><span
                                                class="token punctuation">,</span>
                      <span class="token string">"ranges"</span>: <span class="token punctuation">[</span>
                        <span class="token string">"0xda840f &lt;= from_date &lt;= 0xda840f AND 0xda860f &lt;= to_date &lt;= 0xda860f"</span>
                      <span class="token punctuation">]</span> <span class="token comment">/* ranges */</span>
                    } <span class="token comment">/* range_access_plan */</span><span class="token punctuation">,</span>
                    <span class="token string">"rows_for_plan"</span>: <span class="token number">86</span><span
                                                class="token punctuation">,</span>
                    <span class="token string">"cost_for_plan"</span>: <span class="token number">50.909</span><span
                                                class="token punctuation">,</span>
                    <span class="token string">"chosen"</span>: <span class="token boolean">true</span>
                  } <span class="token comment">/* chosen_range_access_summary */</span>
                } <span class="token comment">/* range_analysis */</span>
              }
            <span class="token punctuation">]</span> <span class="token comment">/* rows_estimation */</span>
          }<span class="token punctuation">,</span>
          {
            <span class="token string">"considered_execution_plans"</span>: <span class="token punctuation">[</span>
              {
                <span class="token string">"plan_prefix"</span>: <span class="token punctuation">[</span>
                <span class="token punctuation">]</span> <span class="token comment">/* plan_prefix */</span><span
                                                class="token punctuation">,</span>
                <span class="token string">"table"</span>: <span class="token string">"`salaries`"</span><span
                                                class="token punctuation">,</span>
                <span class="token string">"best_access_path"</span>: {
                  <span class="token string">"considered_access_paths"</span>: <span class="token punctuation">[</span>
                    {
                      <span class="token string">"access_type"</span>: <span class="token string">"ref"</span><span
                                                class="token punctuation">,</span>
                      <span class="token string">"index"</span>: <span class="token string">"salaries_from_date_to_date_index"</span><span
                                                class="token punctuation">,</span>
                      <span class="token string">"rows"</span>: <span class="token number">86</span><span
                                                class="token punctuation">,</span>
                      <span class="token string">"cost"</span>: <span class="token number">50.412</span><span
                                                class="token punctuation">,</span>
                      <span class="token string">"chosen"</span>: <span class="token boolean">true</span>
                    }<span class="token punctuation">,</span>
                    {
                      <span class="token string">"access_type"</span>: <span class="token string">"range"</span><span
                                                class="token punctuation">,</span>
                      <span class="token string">"range_details"</span>: {
                        <span class="token string">"used_index"</span>: <span class="token string">"salaries_from_date_to_date_index"</span>
                      } <span class="token comment">/* range_details */</span><span class="token punctuation">,</span>
                      <span class="token string">"chosen"</span>: <span class="token boolean">false</span><span
                                                class="token punctuation">,</span>
                      <span class="token string">"cause"</span>: <span
                                                class="token string">"heuristic_index_cheaper"</span>
                    }
                  <span class="token punctuation">]</span> <span
                                                class="token comment">/* considered_access_paths */</span>
                } <span class="token comment">/* best_access_path */</span><span class="token punctuation">,</span>
                <span class="token string">"condition_filtering_pct"</span>: <span class="token number">100</span><span
                                                class="token punctuation">,</span>
                <span class="token string">"rows_for_plan"</span>: <span class="token number">86</span><span
                                                class="token punctuation">,</span>
                <span class="token string">"cost_for_plan"</span>: <span class="token number">50.412</span><span
                                                class="token punctuation">,</span>
                <span class="token string">"chosen"</span>: <span class="token boolean">true</span>
              }
            <span class="token punctuation">]</span> <span class="token comment">/* considered_execution_plans */</span>
          }<span class="token punctuation">,</span>
          {
            <span class="token string">"attaching_conditions_to_tables"</span>: {
              <span class="token string">"original_condition"</span>: <span class="token string">"((`salaries`.`to_date` = DATE'1987-06-26') and (`salaries`.`from_date` = DATE'1986-06-26'))"</span><span
                                                class="token punctuation">,</span>
              <span class="token string">"attached_conditions_computation"</span>: <span
                                                class="token punctuation">[</span>
              <span class="token punctuation">]</span> <span
                                                class="token comment">/* attached_conditions_computation */</span><span
                                                class="token punctuation">,</span>
              <span class="token string">"attached_conditions_summary"</span>: <span class="token punctuation">[</span>
                {
                  <span class="token string">"table"</span>: <span class="token string">"`salaries`"</span><span
                                                class="token punctuation">,</span>
                  <span class="token string">"attached"</span>: <span class="token string">"((`salaries`.`to_date` = DATE'1987-06-26') and (`salaries`.`from_date` = DATE'1986-06-26'))"</span>
                }
              <span class="token punctuation">]</span> <span
                                                class="token comment">/* attached_conditions_summary */</span>
            } <span class="token comment">/* attaching_conditions_to_tables */</span>
          }<span class="token punctuation">,</span>
          {
            <span class="token string">"finalizing_table_conditions"</span>: <span class="token punctuation">[</span>
              {
                <span class="token string">"table"</span>: <span class="token string">"`salaries`"</span><span
                                                class="token punctuation">,</span>
                <span class="token string">"original_table_condition"</span>: <span class="token string">"((`salaries`.`to_date` = DATE'1987-06-26') and (`salaries`.`from_date` = DATE'1986-06-26'))"</span><span
                                                class="token punctuation">,</span>
                <span class="token string">"final_table_condition   "</span>: <span class="token boolean">null</span>
              }
            <span class="token punctuation">]</span> <span
                                                class="token comment">/* finalizing_table_conditions */</span>
          }<span class="token punctuation">,</span>
          {
            <span class="token string">"refine_plan"</span>: <span class="token punctuation">[</span>
              {
                <span class="token string">"table"</span>: <span class="token string">"`salaries`"</span>
              }
            <span class="token punctuation">]</span> <span class="token comment">/* refine_plan */</span>
          }
        <span class="token punctuation">]</span> <span class="token comment">/* steps */</span>
      } <span class="token comment">/* join_optimization */</span>
    }<span class="token punctuation">,</span>
    {
      <span class="token string">"join_execution"</span>: {
        <span class="token string">"select#"</span>: <span class="token number">1</span><span class="token punctuation">,</span>
        <span class="token string">"steps"</span>: <span class="token punctuation">[</span>
        <span class="token punctuation">]</span> <span class="token comment">/* steps */</span>
      } <span class="token comment">/* join_execution */</span>
    }
  <span class="token punctuation">]</span> <span class="token comment">/* steps */</span>
}
MISSING_BYTES_BEYOND_MAX_MEM_SIZE: <span class="token number">0</span>
          INSUFFICIENT_PRIVILEGES: <span class="token number">0</span>
<span class="token number">1</span> <span class="token keyword">row</span> <span class="token operator">in</span> <span
                                                class="token keyword">set</span> <span
                                                class="token punctuation">(</span><span class="token number">0.00</span> sec<span
                                                class="token punctuation">)</span>
</code></pre>
                                </li>
                                <li>
                                    <p>分析完成，关闭OPTIMIZER_TRACE</p>
                                    <pre class=" language-sql"><code class="prism  language-sql"><span
                                            class="token keyword">SET</span> optimizer_trace<span
                                            class="token operator">=</span><span
                                            class="token string">"enabled=off"</span><span
                                            class="token punctuation">;</span>
</code></pre>
                                </li>
                            </ul>
                        </div>
                        <div class="cl-preview-section"><h2 id="optimizer_trace结果分析">OPTIMIZER_TRACE结果分析</h2>
                        </div>
                        <div class="cl-preview-section"><p>由上面的结果可知，OPTIMIZER_TRACE有四个字段：</p>
                        </div>
                        <div class="cl-preview-section">
                            <ul>
                                <li>QUERY：查询语句</li>
                                <li>TRACE：QUERY字段对应语句的跟踪信息</li>
                                <li>MISSING_BYTES_BEYOND_MAX_MEM_SIZE：跟踪信息过长时，被截断的跟踪信息的字节数。</li>
                                <li>INSUFFICIENT_PRIVILEGES：执行跟踪语句的用户是否有查看对象的权限。当不具有权限时，该列信息为1且TRACE字段为空，一般在调用带有SQL
                                    SECURITY DEFINER的视图或者是存储过程的情况下，会出现此问题。
                                </li>
                            </ul>
                        </div>
                        <div class="cl-preview-section">
                            <blockquote>
                                <p><strong>TIPS</strong></p>
                                <p>参考： <a href="https://dev.mysql.com/doc/refman/8.0/en/optimizer-trace-table.html">https://dev.mysql.com/doc/refman/8.0/en/optimizer-trace-table.html</a>
                                </p>
                            </blockquote>
                        </div>
                        <div class="cl-preview-section"><p>最核心的是TRACE字段的内容。我们逐段分析：</p>
                        </div>
                        <div class="cl-preview-section"><h3 id="join_preparation">join_preparation</h3>
                        </div>
                        <div class="cl-preview-section"><p>join_preparation段落展示了准备阶段的执行过程。</p>
                        </div>
                        <div class="cl-preview-section"><pre class=" language-sql"><code class="prism  language-sql">{
  <span class="token string">"join_preparation"</span>: {
    <span class="token string">"select#"</span>: <span class="token number">1</span><span
                                    class="token punctuation">,</span>
    <span class="token string">"steps"</span>: <span class="token punctuation">[</span>
      {
        <span class="token comment">-- 对比下原始语句，可以知道，这一步做了个格式化。</span>
        <span class="token string">"expanded_query"</span>: <span class="token string">"/* select#1 */ select `salaries`.`emp_no` AS `emp_no`,`salaries`.`salary` AS `salary`,`salaries`.`from_date` AS `from_date`,`salaries`.`to_date` AS `to_date` from `salaries` where ((`salaries`.`from_date` = '1986-06-26') and (`salaries`.`to_date` = '1987-06-26'))"</span>
      }
    <span class="token punctuation">]</span>
    <span class="token comment">/* steps */</span>
  }
  <span class="token comment">/* join_preparation */</span>
}
</code></pre>
                        </div>
                        <div class="cl-preview-section"><h3 id="join_optimization">join_optimization</h3>
                        </div>
                        <div class="cl-preview-section"><p>join_optimization展示了优化阶段的执行过程，是分析OPTIMIZER
                            TRACE的重点。这段内容超级长，而且分了好多步骤，不妨按照步骤逐段分析：</p>
                        </div>
                        <div class="cl-preview-section"><h4 id="condition_processing">condition_processing</h4>
                        </div>
                        <div class="cl-preview-section"><p>该段用来做条件处理，主要对WHERE条件进行优化处理。</p>
                        </div>
                        <div class="cl-preview-section"><pre class=" language-json"><code
                                class="prism  language-json"><span
                                class="token string">"condition_processing"</span><span
                                class="token punctuation">:</span> <span class="token punctuation">{</span>
  <span class="token string">"condition"</span><span class="token punctuation">:</span> <span class="token string">"WHERE"</span><span
                                    class="token punctuation">,</span>
  <span class="token string">"original_condition"</span><span class="token punctuation">:</span> <span
                                    class="token string">"((`salaries`.`from_date` = '1986-06-26') and (`salaries`.`to_date` = '1987-06-26'))"</span><span
                                    class="token punctuation">,</span>
  <span class="token string">"steps"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
    <span class="token punctuation">{</span>
      <span class="token string">"transformation"</span><span class="token punctuation">:</span> <span
                                    class="token string">"equality_propagation"</span><span
                                    class="token punctuation">,</span>
      <span class="token string">"resulting_condition"</span><span class="token punctuation">:</span> "<span
                                    class="token punctuation">(</span>multiple <span class="token function">equal</span><span
                                    class="token punctuation">(</span><span
                                    class="token string">'1986-06-26'</span><span
                                    class="token punctuation">,</span> <span class="token template-string"><span
                                    class="token string">`salaries`</span></span><span
                                    class="token punctuation">.</span><span class="token template-string"><span
                                    class="token string">`from_date`</span></span><span
                                    class="token punctuation">)</span> and multiple <span
                                    class="token function">equal</span><span class="token punctuation">(</span><span
                                    class="token string">'1987-06-26'</span><span
                                    class="token punctuation">,</span> <span class="token template-string"><span
                                    class="token string">`salaries`</span></span><span
                                    class="token punctuation">.</span><span class="token template-string"><span
                                    class="token string">`to_date`</span></span><span class="token punctuation">)</span><span
                                    class="token punctuation">)</span>"
    <span class="token punctuation">}</span><span class="token punctuation">,</span>
    <span class="token punctuation">{</span>
      <span class="token string">"transformation"</span><span class="token punctuation">:</span> <span
                                    class="token string">"constant_propagation"</span><span
                                    class="token punctuation">,</span>
      <span class="token string">"resulting_condition"</span><span class="token punctuation">:</span> "<span
                                    class="token punctuation">(</span>multiple <span class="token function">equal</span><span
                                    class="token punctuation">(</span><span
                                    class="token string">'1986-06-26'</span><span
                                    class="token punctuation">,</span> <span class="token template-string"><span
                                    class="token string">`salaries`</span></span><span
                                    class="token punctuation">.</span><span class="token template-string"><span
                                    class="token string">`from_date`</span></span><span
                                    class="token punctuation">)</span> and multiple <span
                                    class="token function">equal</span><span class="token punctuation">(</span><span
                                    class="token string">'1987-06-26'</span><span
                                    class="token punctuation">,</span> <span class="token template-string"><span
                                    class="token string">`salaries`</span></span><span
                                    class="token punctuation">.</span><span class="token template-string"><span
                                    class="token string">`to_date`</span></span><span class="token punctuation">)</span><span
                                    class="token punctuation">)</span>"
    <span class="token punctuation">}</span><span class="token punctuation">,</span>
    <span class="token punctuation">{</span>
      <span class="token string">"transformation"</span><span class="token punctuation">:</span> <span
                                    class="token string">"trivial_condition_removal"</span><span
                                    class="token punctuation">,</span>
      <span class="token string">"resulting_condition"</span><span class="token punctuation">:</span> "<span
                                    class="token punctuation">(</span>multiple <span class="token function">equal</span><span
                                    class="token punctuation">(</span>DATE<span class="token string">'1986-06-26'</span><span
                                    class="token punctuation">,</span> <span class="token template-string"><span
                                    class="token string">`salaries`</span></span><span
                                    class="token punctuation">.</span><span class="token template-string"><span
                                    class="token string">`from_date`</span></span><span
                                    class="token punctuation">)</span> and multiple <span
                                    class="token function">equal</span><span class="token punctuation">(</span>DATE<span
                                    class="token string">'1987-06-26'</span><span
                                    class="token punctuation">,</span> <span class="token template-string"><span
                                    class="token string">`salaries`</span></span><span
                                    class="token punctuation">.</span><span class="token template-string"><span
                                    class="token string">`to_date`</span></span><span class="token punctuation">)</span><span
                                    class="token punctuation">)</span>"
    <span class="token punctuation">}</span>
  <span class="token punctuation">]</span> <span class="token comment">/* steps */</span>
<span class="token punctuation">}</span> <span class="token comment">/* condition_processing */</span>
</code></pre>
                        </div>
                        <div class="cl-preview-section"><p>其中：</p>
                        </div>
                        <div class="cl-preview-section">
                            <ul>
                                <li>condition：优化对象类型。WHERE条件句或者是HAVING条件句</li>
                                <li>original_condition：优化前的原始语句</li>
                                <li>
                                    steps：主要包括三步，分别是quality_propagation（等值条件句转换），constant_propagation（常量条件句转换），trivial_condition_removal（无效条件移除的转换）
                                    <ul>
                                        <li>transformation：转换类型句</li>
                                        <li>resulting_condition：转换之后的结果输出</li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                        <div class="cl-preview-section"><h4 id="substitute_generated_columns">
                            substitute_generated_columns</h4>
                        </div>
                        <div class="cl-preview-section"><p>substitute_generated_columns用于替换虚拟生成列</p>
                        </div>
                        <div class="cl-preview-section"><pre class=" language-sql"><code
                                class="prism  language-sql"><span
                                class="token string">"substitute_generated_columns"</span>: {
} <span class="token comment">/* substitute_generated_columns */</span>
</code></pre>
                        </div>
                        <div class="cl-preview-section"><h4 id="table_dependencies">table_dependencies</h4>
                        </div>
                        <div class="cl-preview-section"><p>分析表之间的依赖关系</p>
                        </div>
                        <div class="cl-preview-section"><pre class=" language-json"><code
                                class="prism  language-json"><span class="token punctuation">{</span>
  <span class="token string">"table_dependencies"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
    <span class="token punctuation">{</span>
      <span class="token string">"table"</span><span class="token punctuation">:</span> <span class="token string">"`salaries`"</span><span
                                    class="token punctuation">,</span>
      <span class="token string">"row_may_be_null"</span><span class="token punctuation">:</span> <span
                                    class="token boolean">false</span><span class="token punctuation">,</span>
      <span class="token string">"map_bit"</span><span class="token punctuation">:</span> <span
                                    class="token number">0</span><span class="token punctuation">,</span>
      <span class="token string">"depends_on_map_bits"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
      <span class="token punctuation">]</span> <span class="token comment">/* depends_on_map_bits */</span>
    <span class="token punctuation">}</span>
  <span class="token punctuation">]</span> <span class="token comment">/* table_dependencies */</span>
<span class="token punctuation">}</span>
</code></pre>
                        </div>
                        <div class="cl-preview-section"><p>其中：</p>
                        </div>
                        <div class="cl-preview-section">
                            <ul>
                                <li>table：涉及的表名，如果有别名，也会展示出来</li>
                                <li>row_may_be_null：行是否可能为NULL，这里是指JOIN操作之后，这张表里的数据是不是可能为NULL。如果语句中使用了LEFT
                                    JOIN，则后一张表的row_may_be_null会显示为true
                                </li>
                                <li>map_bit：表的映射编号，从0开始递增</li>
                                <li>depends_on_map_bits：依赖的映射表。主要是当使用STRAIGHT_JOIN强行控制连接顺序或者LEFT JOIN/RIGHT
                                    JOIN有顺序差别时，会在depends_on_map_bits中展示前置表的map_bit值。
                                </li>
                            </ul>
                        </div>
                        <div class="cl-preview-section"><h4 id="ref_optimizer_key_uses">ref_optimizer_key_uses</h4>
                        </div>
                        <div class="cl-preview-section"><p>列出所有可用的ref类型的索引。如果使用了组合索引的多个部分（例如本例，用到了index(from_date,
                            to_date) 的多列索引），则会在ref_optimizer_key_uses下列出多个元素，每个元素中会列出ref使用的索引及对应值。</p>
                        </div>
                        <div class="cl-preview-section"><pre class=" language-json"><code
                                class="prism  language-json"><span class="token punctuation">{</span>
  <span class="token string">"ref_optimizer_key_uses"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
    <span class="token punctuation">{</span>
      <span class="token string">"table"</span><span class="token punctuation">:</span> <span class="token string">"`salaries`"</span><span
                                    class="token punctuation">,</span>
      <span class="token string">"field"</span><span class="token punctuation">:</span> <span class="token string">"from_date"</span><span
                                    class="token punctuation">,</span>
      <span class="token string">"equals"</span><span class="token punctuation">:</span> <span class="token string">"DATE'1986-06-26'"</span><span
                                    class="token punctuation">,</span>
      <span class="token string">"null_rejecting"</span><span class="token punctuation">:</span> <span
                                    class="token boolean">false</span>
    <span class="token punctuation">}</span><span class="token punctuation">,</span>
    <span class="token punctuation">{</span>
      <span class="token string">"table"</span><span class="token punctuation">:</span> <span class="token string">"`salaries`"</span><span
                                    class="token punctuation">,</span>
      <span class="token string">"field"</span><span class="token punctuation">:</span> <span class="token string">"to_date"</span><span
                                    class="token punctuation">,</span>
      <span class="token string">"equals"</span><span class="token punctuation">:</span> <span class="token string">"DATE'1987-06-26'"</span><span
                                    class="token punctuation">,</span>
      <span class="token string">"null_rejecting"</span><span class="token punctuation">:</span> <span
                                    class="token boolean">false</span>
    <span class="token punctuation">}</span>
  <span class="token punctuation">]</span> <span class="token comment">/* ref_optimizer_key_uses */</span>
<span class="token punctuation">}</span>
</code></pre>
                        </div>
                        <div class="cl-preview-section"><h4 id="rows_estimation">rows_estimation</h4>
                        </div>
                        <div class="cl-preview-section"><p>顾名思义，用于估算需要扫描的记录数。</p>
                        </div>
                        <div class="cl-preview-section"><pre class=" language-json"><code
                                class="prism  language-json"><span class="token punctuation">{</span>
  <span class="token string">"rows_estimation"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
    <span class="token punctuation">{</span>
      <span class="token string">"table"</span><span class="token punctuation">:</span> <span class="token string">"`salaries`"</span><span
                                    class="token punctuation">,</span>
      <span class="token string">"range_analysis"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">{</span>
        <span class="token string">"table_scan"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">{</span>
          <span class="token string">"rows"</span><span class="token punctuation">:</span> <span class="token number">2838216</span><span
                                    class="token punctuation">,</span>
          <span class="token string">"cost"</span><span class="token punctuation">:</span> <span class="token number">286799</span>
        <span class="token punctuation">}</span> <span class="token comment">/* table_scan */</span><span
                                    class="token punctuation">,</span>
        <span class="token string">"potential_range_indexes"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
          <span class="token punctuation">{</span>
            <span class="token string">"index"</span><span class="token punctuation">:</span> <span
                                    class="token string">"PRIMARY"</span><span class="token punctuation">,</span>
            <span class="token string">"usable"</span><span class="token punctuation">:</span> <span
                                    class="token boolean">false</span><span class="token punctuation">,</span>
            <span class="token string">"cause"</span><span class="token punctuation">:</span> <span
                                    class="token string">"not_applicable"</span>
          <span class="token punctuation">}</span><span class="token punctuation">,</span>
          <span class="token punctuation">{</span>
            <span class="token string">"index"</span><span class="token punctuation">:</span> <span
                                    class="token string">"salaries_from_date_to_date_index"</span><span
                                    class="token punctuation">,</span>
            <span class="token string">"usable"</span><span class="token punctuation">:</span> <span
                                    class="token boolean">true</span><span class="token punctuation">,</span>
            <span class="token string">"key_parts"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
              <span class="token string">"from_date"</span><span class="token punctuation">,</span>
              <span class="token string">"to_date"</span><span class="token punctuation">,</span>
              <span class="token string">"emp_no"</span>
            <span class="token punctuation">]</span> <span class="token comment">/* key_parts */</span>
          <span class="token punctuation">}</span>
        <span class="token punctuation">]</span> <span class="token comment">/* potential_range_indexes */</span><span
                                    class="token punctuation">,</span>
        <span class="token string">"setup_range_conditions"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
        <span class="token punctuation">]</span> <span class="token comment">/* setup_range_conditions */</span><span
                                    class="token punctuation">,</span>
        <span class="token string">"group_index_range"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">{</span>
          <span class="token string">"chosen"</span><span class="token punctuation">:</span> <span
                                    class="token boolean">false</span><span class="token punctuation">,</span>
          <span class="token string">"cause"</span><span class="token punctuation">:</span> <span class="token string">"not_group_by_or_distinct"</span>
        <span class="token punctuation">}</span> <span class="token comment">/* group_index_range */</span><span
                                    class="token punctuation">,</span>
        <span class="token string">"skip_scan_range"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">{</span>
          <span class="token string">"potential_skip_scan_indexes"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
            <span class="token punctuation">{</span>
              <span class="token string">"index"</span><span class="token punctuation">:</span> <span
                                    class="token string">"salaries_from_date_to_date_index"</span><span
                                    class="token punctuation">,</span>
              <span class="token string">"usable"</span><span class="token punctuation">:</span> <span
                                    class="token boolean">false</span><span class="token punctuation">,</span>
              <span class="token string">"cause"</span><span class="token punctuation">:</span> <span
                                    class="token string">"query_references_nonkey_column"</span>
            <span class="token punctuation">}</span>
          <span class="token punctuation">]</span> <span class="token comment">/* potential_skip_scan_indexes */</span>
        <span class="token punctuation">}</span> <span class="token comment">/* skip_scan_range */</span><span
                                    class="token punctuation">,</span>
        <span class="token string">"analyzing_range_alternatives"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">{</span>
          <span class="token string">"range_scan_alternatives"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
            <span class="token punctuation">{</span>
              <span class="token string">"index"</span><span class="token punctuation">:</span> <span
                                    class="token string">"salaries_from_date_to_date_index"</span><span
                                    class="token punctuation">,</span>
              <span class="token string">"ranges"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
                <span class="token string">"0xda840f &lt;= from_date &lt;= 0xda840f AND 0xda860f &lt;= to_date &lt;= 0xda860f"</span>
              <span class="token punctuation">]</span> <span class="token comment">/* ranges */</span><span
                                    class="token punctuation">,</span>
              <span class="token string">"index_dives_for_eq_ranges"</span><span
                                    class="token punctuation">:</span> <span class="token boolean">true</span><span
                                    class="token punctuation">,</span>
              <span class="token string">"rowid_ordered"</span><span class="token punctuation">:</span> <span
                                    class="token boolean">true</span><span class="token punctuation">,</span>
              <span class="token string">"using_mrr"</span><span class="token punctuation">:</span> <span
                                    class="token boolean">false</span><span class="token punctuation">,</span>
              <span class="token string">"index_only"</span><span class="token punctuation">:</span> <span
                                    class="token boolean">false</span><span class="token punctuation">,</span>
              <span class="token string">"rows"</span><span class="token punctuation">:</span> <span
                                    class="token number">86</span><span class="token punctuation">,</span>
              <span class="token string">"cost"</span><span class="token punctuation">:</span> <span
                                    class="token number">50.909</span><span class="token punctuation">,</span>
              <span class="token string">"chosen"</span><span class="token punctuation">:</span> <span
                                    class="token boolean">true</span>
            <span class="token punctuation">}</span>
          <span class="token punctuation">]</span> <span class="token comment">/* range_scan_alternatives */</span><span
                                    class="token punctuation">,</span>
          <span class="token string">"analyzing_roworder_intersect"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">{</span>
            <span class="token string">"usable"</span><span class="token punctuation">:</span> <span
                                    class="token boolean">false</span><span class="token punctuation">,</span>
            <span class="token string">"cause"</span><span class="token punctuation">:</span> <span
                                    class="token string">"too_few_roworder_scans"</span>
          <span class="token punctuation">}</span> <span class="token comment">/* analyzing_roworder_intersect */</span>
        <span class="token punctuation">}</span> <span
                                    class="token comment">/* analyzing_range_alternatives */</span><span
                                    class="token punctuation">,</span>
        <span class="token string">"chosen_range_access_summary"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">{</span>
          <span class="token string">"range_access_plan"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">{</span>
            <span class="token string">"type"</span><span class="token punctuation">:</span> <span class="token string">"range_scan"</span><span
                                    class="token punctuation">,</span>
            <span class="token string">"index"</span><span class="token punctuation">:</span> <span
                                    class="token string">"salaries_from_date_to_date_index"</span><span
                                    class="token punctuation">,</span>
            <span class="token string">"rows"</span><span class="token punctuation">:</span> <span class="token number">86</span><span
                                    class="token punctuation">,</span>
            <span class="token string">"ranges"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
              <span class="token string">"0xda840f &lt;= from_date &lt;= 0xda840f AND 0xda860f &lt;= to_date &lt;= 0xda860f"</span>
            <span class="token punctuation">]</span> <span class="token comment">/* ranges */</span>
          <span class="token punctuation">}</span> <span class="token comment">/* range_access_plan */</span><span
                                    class="token punctuation">,</span>
          <span class="token string">"rows_for_plan"</span><span class="token punctuation">:</span> <span
                                    class="token number">86</span><span class="token punctuation">,</span>
          <span class="token string">"cost_for_plan"</span><span class="token punctuation">:</span> <span
                                    class="token number">50.909</span><span class="token punctuation">,</span>
          <span class="token string">"chosen"</span><span class="token punctuation">:</span> <span
                                    class="token boolean">true</span>
        <span class="token punctuation">}</span> <span class="token comment">/* chosen_range_access_summary */</span>
      <span class="token punctuation">}</span> <span class="token comment">/* range_analysis */</span>
    <span class="token punctuation">}</span>
  <span class="token punctuation">]</span> <span class="token comment">/* rows_estimation */</span>
<span class="token punctuation">}</span>
</code></pre>
                        </div>
                        <div class="cl-preview-section"><p>其中：</p>
                        </div>
                        <div class="cl-preview-section">
                            <ul>
                                <li>
                                    <p>table：表名</p>
                                </li>
                                <li>
                                    <p>range_analysis：</p>
                                    <ul>
                                        <li>
                                            <p>table_scan：如果全表扫描的话，需要扫描多少行（row，2838216），以及需要的代价（cost，286799）</p>
                                        </li>
                                        <li>
                                            <p>
                                                potential_range_indexes：列出表中所有的索引并分析其是否可用。如果不可用的话，会列出不可用的原因是什么；如果可用会列出索引中可用的字段；</p>
                                        </li>
                                        <li>
                                            <p>setup_range_conditions：如果有可下推的条件，则带条件考虑范围查询</p>
                                        </li>
                                        <li>
                                            <p>group_index_range：当使用了GROUP BY或DISTINCT时，是否有合适的索引可用。当未使用GROUP
                                                BY或DISTINCT时，会显示chosen=false, cause=not_group_by_or_distinct；如使用了GROUP
                                                BY或DISTINCT，但是多表查询时，会显示chosen=false，cause
                                                =not_single_table。其他情况下会尝试分析可用的索引（potential_group_range_indexes）并计算对应的扫描行数及其所需代价</p>
                                        </li>
                                        <li>
                                            <p>skip_scan_range：是否使用了skip scan</p>
                                            <blockquote>
                                                <p><strong>TIPS</strong></p>
                                                <p>skip_scan_range是MySQL 8.0的新特性，感兴趣的可详见 <a
                                                        href="https://blog.csdn.net/weixin_43970890/article/details/89494915">https://blog.csdn.net/weixin_43970890/article/details/89494915</a>
                                                </p>
                                            </blockquote>
                                        </li>
                                        <li>
                                            <p>analyzing_range_alternatives：分析各个索引的使用成本</p>
                                            <ul>
                                                <li>range_scan_alternatives：range扫描分析
                                                    <ul>
                                                        <li>index：索引名</li>
                                                        <li>ranges：range扫描的条件范围</li>
                                                        <li>index_dives_for_eq_ranges：是否使用了index
                                                            dive，该值会被参数eq_range_index_dive_limit变量值影响。
                                                        </li>
                                                        <li>rowid_ordered：该range扫描的结果集是否根据PK值进行排序</li>
                                                        <li>using_mrr：是否使用了mrr</li>
                                                        <li>index_only：表示是否使用了覆盖索引</li>
                                                        <li>rows：扫描的行数</li>
                                                        <li>cost：索引的使用成本</li>
                                                        <li>chosen：表示是否使用了该索引</li>
                                                    </ul>
                                                </li>
                                                <li>analyzing_roworder_intersect：分析是否使用了索引合并（index
                                                    merge），如果未使用，会在cause中展示原因；如果使用了索引合并，会在该部分展示索引合并的代价。
                                                </li>
                                            </ul>
                                        </li>
                                        <li>
                                            <p>
                                                chosen_range_access_summary：在前一个步骤中分析了各类索引使用的方法及代价，得出了一定的中间结果之后，在summary阶段汇总前一阶段的中间结果确认最后的方案</p>
                                            <ul>
                                                <li>range_access_plan：range扫描最终选择的执行计划。
                                                    <ul>
                                                        <li>type：展示执行计划的type，如果使用了索引合并，则会显示index_roworder_intersect</li>
                                                        <li>index：索引名</li>
                                                        <li>rows：扫描的行数</li>
                                                        <li>ranges：range扫描的条件范围</li>
                                                    </ul>
                                                </li>
                                                <li>rows_for_plan：该执行计划的扫描行数</li>
                                                <li>cost_for_plan：该执行计划的执行代价</li>
                                                <li>chosen：是否选择该执行计划</li>
                                            </ul>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                        <div class="cl-preview-section"><h4 id="considered_execution_plans">
                            considered_execution_plans</h4>
                        </div>
                        <div class="cl-preview-section"><p>负责对比各可行计划的开销，并选择相对最优的执行计划。</p>
                        </div>
                        <div class="cl-preview-section"><pre class=" language-json"><code
                                class="prism  language-json"><span class="token punctuation">{</span>
  <span class="token string">"considered_execution_plans"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
    <span class="token punctuation">{</span>
      <span class="token string">"plan_prefix"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
      <span class="token punctuation">]</span> <span class="token comment">/* plan_prefix */</span><span
                                    class="token punctuation">,</span>
      <span class="token string">"table"</span><span class="token punctuation">:</span> <span class="token string">"`salaries`"</span><span
                                    class="token punctuation">,</span>
      <span class="token string">"best_access_path"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">{</span>
        <span class="token string">"considered_access_paths"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
          <span class="token punctuation">{</span>
            <span class="token string">"access_type"</span><span class="token punctuation">:</span> <span
                                    class="token string">"ref"</span><span class="token punctuation">,</span>
            <span class="token string">"index"</span><span class="token punctuation">:</span> <span
                                    class="token string">"salaries_from_date_to_date_index"</span><span
                                    class="token punctuation">,</span>
            <span class="token string">"rows"</span><span class="token punctuation">:</span> <span class="token number">86</span><span
                                    class="token punctuation">,</span>
            <span class="token string">"cost"</span><span class="token punctuation">:</span> <span class="token number">50.412</span><span
                                    class="token punctuation">,</span>
            <span class="token string">"chosen"</span><span class="token punctuation">:</span> <span
                                    class="token boolean">true</span>
          <span class="token punctuation">}</span><span class="token punctuation">,</span>
          <span class="token punctuation">{</span>
            <span class="token string">"access_type"</span><span class="token punctuation">:</span> <span
                                    class="token string">"range"</span><span class="token punctuation">,</span>
            <span class="token string">"range_details"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">{</span>
              <span class="token string">"used_index"</span><span class="token punctuation">:</span> <span
                                    class="token string">"salaries_from_date_to_date_index"</span>
            <span class="token punctuation">}</span> <span class="token comment">/* range_details */</span><span
                                    class="token punctuation">,</span>
            <span class="token string">"chosen"</span><span class="token punctuation">:</span> <span
                                    class="token boolean">false</span><span class="token punctuation">,</span>
            <span class="token string">"cause"</span><span class="token punctuation">:</span> <span
                                    class="token string">"heuristic_index_cheaper"</span>
          <span class="token punctuation">}</span>
        <span class="token punctuation">]</span> <span class="token comment">/* considered_access_paths */</span>
      <span class="token punctuation">}</span> <span class="token comment">/* best_access_path */</span><span
                                    class="token punctuation">,</span>
      <span class="token string">"condition_filtering_pct"</span><span class="token punctuation">:</span> <span
                                    class="token number">100</span><span class="token punctuation">,</span>
      <span class="token string">"rows_for_plan"</span><span class="token punctuation">:</span> <span
                                    class="token number">86</span><span class="token punctuation">,</span>
      <span class="token string">"cost_for_plan"</span><span class="token punctuation">:</span> <span
                                    class="token number">50.412</span><span class="token punctuation">,</span>
      <span class="token string">"chosen"</span><span class="token punctuation">:</span> <span class="token boolean">true</span>
    <span class="token punctuation">}</span>
  <span class="token punctuation">]</span> <span class="token comment">/* considered_execution_plans */</span>
<span class="token punctuation">}</span>
</code></pre>
                        </div>
                        <div class="cl-preview-section"><p>其中：</p>
                        </div>
                        <div class="cl-preview-section">
                            <ul>
                                <li>plan_prefix：当前计划的前置执行计划。</li>
                                <li>table：涉及的表名，如果有别名，也会展示出来</li>
                                <li>best_access_path：通过对比considered_access_paths，选择一个最优的访问路径
                                    <ul>
                                        <li>considered_access_paths：当前考虑的访问路径
                                            <ul>
                                                <li>access_type：使用索引的方式，可参考explain中的type字段</li>
                                                <li>index：索引</li>
                                                <li>rows：行数</li>
                                                <li>cost：开销</li>
                                                <li>chosen：是否选用这种执行路径</li>
                                            </ul>
                                        </li>
                                    </ul>
                                </li>
                                <li>condition_filtering_pct：类似于explain的filtered列，是一个估算值</li>
                                <li>rows_for_plan：执行计划最终的扫描行数，由considered_access_paths.rows X
                                    condition_filtering_pct计算获得。
                                </li>
                                <li>cost_for_plan：执行计划的代价，由considered_access_paths.cost相加获得</li>
                                <li>chosen：是否选择了该执行计划</li>
                            </ul>
                        </div>
                        <div class="cl-preview-section"><h4 id="attaching_conditions_to_tables">
                            attaching_conditions_to_tables</h4>
                        </div>
                        <div class="cl-preview-section"><p>
                            基于considered_execution_plans中选择的执行计划，改造原有where条件，并针对表增加适当的附加条件，以便于单表数据的筛选。</p>
                        </div>
                        <div class="cl-preview-section">
                            <blockquote>
                                <p><strong>TIPS</strong></p>
                                <ul>
                                    <li>这部分条件的增加主要是为了便于ICP（索引条件下推），但ICP是否开启并不影响这部分内容的构造。</li>
                                    <li>ICP参考文档：<a href="https://www.cnblogs.com/Terry-Wu/p/9273177.html">https://www.cnblogs.com/Terry-Wu/p/9273177.html</a>
                                    </li>
                                </ul>
                            </blockquote>
                        </div>
                        <div class="cl-preview-section"><pre class=" language-json"><code
                                class="prism  language-json"><span class="token punctuation">{</span>
  <span class="token string">"attaching_conditions_to_tables"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">{</span>
    <span class="token string">"original_condition"</span><span class="token punctuation">:</span> <span
                                    class="token string">"((`salaries`.`to_date` = DATE'1987-06-26') and (`salaries`.`from_date` = DATE'1986-06-26'))"</span><span
                                    class="token punctuation">,</span>
    <span class="token string">"attached_conditions_computation"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
    <span class="token punctuation">]</span> <span
                                    class="token comment">/* attached_conditions_computation */</span><span
                                    class="token punctuation">,</span>
    <span class="token string">"attached_conditions_summary"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
      <span class="token punctuation">{</span>
        <span class="token string">"table"</span><span class="token punctuation">:</span> <span class="token string">"`salaries`"</span><span
                                    class="token punctuation">,</span>
        <span class="token string">"attached"</span><span class="token punctuation">:</span> <span class="token string">"((`salaries`.`to_date` = DATE'1987-06-26') and (`salaries`.`from_date` = DATE'1986-06-26'))"</span>
      <span class="token punctuation">}</span>
    <span class="token punctuation">]</span> <span class="token comment">/* attached_conditions_summary */</span>
  <span class="token punctuation">}</span> <span class="token comment">/* attaching_conditions_to_tables */</span>
<span class="token punctuation">}</span>
</code></pre>
                        </div>
                        <div class="cl-preview-section"><p>其中：</p>
                        </div>
                        <div class="cl-preview-section">
                            <ul>
                                <li>original_condition：原始的条件语句</li>
                                <li>
                                    attached_conditions_computation：使用启发式算法计算已使用的索引，如果已使用的索引的访问类型是ref，则计算用range能否使用组合索引中更多的列，如果可以，则用range的方式替换ref。
                                </li>
                                <li>attached_conditions_summary：附加之后的情况汇总
                                    <ul>
                                        <li>table：表名</li>
                                        <li>attached：附加的条件或原语句中能直接下推给单表筛选的条件。</li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                        <div class="cl-preview-section"><h4 id="finalizing_table_conditions">
                            finalizing_table_conditions</h4>
                        </div>
                        <div class="cl-preview-section"><p>最终的、经过优化后的表条件。</p>
                        </div>
                        <div class="cl-preview-section"><pre class=" language-json"><code
                                class="prism  language-json"><span class="token punctuation">{</span>
  <span class="token string">"finalizing_table_conditions"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
    <span class="token punctuation">{</span>
      <span class="token string">"table"</span><span class="token punctuation">:</span> <span class="token string">"`salaries`"</span><span
                                    class="token punctuation">,</span>
      <span class="token string">"original_table_condition"</span><span class="token punctuation">:</span> <span
                                    class="token string">"((`salaries`.`to_date` = DATE'1987-06-26') and (`salaries`.`from_date` = DATE'1986-06-26'))"</span><span
                                    class="token punctuation">,</span>
      <span class="token string">"final_table_condition   "</span><span class="token punctuation">:</span> <span
                                    class="token keyword">null</span>
    <span class="token punctuation">}</span>
  <span class="token punctuation">]</span> <span class="token comment">/* finalizing_table_conditions */</span>
<span class="token punctuation">}</span>
</code></pre>
                        </div>
                        <div class="cl-preview-section"><h4 id="refine_plan">refine_plan</h4>
                        </div>
                        <div class="cl-preview-section"><p>改善执行计划：</p>
                        </div>
                        <div class="cl-preview-section"><pre class=" language-json"><code
                                class="prism  language-json"><span class="token punctuation">{</span>
  <span class="token string">"refine_plan"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
    <span class="token punctuation">{</span>
      <span class="token string">"table"</span><span class="token punctuation">:</span> <span class="token string">"`salaries`"</span>
    <span class="token punctuation">}</span>
  <span class="token punctuation">]</span> <span class="token comment">/* refine_plan */</span>
<span class="token punctuation">}</span>
</code></pre>
                        </div>
                        <div class="cl-preview-section"><p>其中：</p>
                        </div>
                        <div class="cl-preview-section">
                            <ul>
                                <li>table：表名及别名</li>
                            </ul>
                        </div>
                        <div class="cl-preview-section"><h3 id="join_execution">join_execution</h3>
                        </div>
                        <div class="cl-preview-section"><p>join_execution段落展示了执行阶段的执行过程。</p>
                        </div>
                        <div class="cl-preview-section"><pre class=" language-json"><code
                                class="prism  language-json"><span class="token string">"join_execution"</span><span
                                class="token punctuation">:</span> <span class="token punctuation">{</span>
  <span class="token string">"select#"</span><span class="token punctuation">:</span> <span
                                    class="token number">1</span><span class="token punctuation">,</span>
  <span class="token string">"steps"</span><span class="token punctuation">:</span> <span
                                    class="token punctuation">[</span>
  <span class="token punctuation">]</span> <span class="token comment">/* steps */</span>
<span class="token punctuation">}</span>
</code></pre>
                        </div>
                        <div class="cl-preview-section"><h3 id="参考文档">参考文档</h3>
                        </div>
                        <div class="cl-preview-section">
                            <ul>
                                <li><a href="https://dev.mysql.com/doc/internals/en/optimizer-tracing.html">Tracing the
                                    Optimizer</a></li>
                                <li>
                                    <a href="http://blog.itpub.net/28218939/viewspace-2658978/">手把手教你认识OPTIMIZER_TRACE</a>
                                </li>
                                <li><a href="http://blog.itpub.net/29863023/viewspace-2565095/">MYSQL
                                    sql执行过程的一些跟踪分析(二.mysql优化器追踪分析)</a></li>
                                <li><a href="https://www.cnblogs.com/hbbbs/articles/12737077.html">使用 Trace 进行执行计划分析</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>